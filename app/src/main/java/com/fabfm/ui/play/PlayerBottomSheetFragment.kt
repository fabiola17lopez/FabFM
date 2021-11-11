package com.fabfm.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fabfm.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.util.Util
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val ARG_MEDIA_URL = "MediaUrl"
class PlayerBottomSheetFragment: BottomSheetDialogFragment() {

    companion object {
        fun newInstance(url: String): PlayerBottomSheetFragment =
            PlayerBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MEDIA_URL, url)
                }
            }
    }

    private var _binding: FragmentPlayerBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var mediaUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaUrl = arguments?.getString(ARG_MEDIA_URL)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        binding.closeButton.setOnClickListener { dismiss() }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.videoView.player = exoPlayer
                // From my understanding after some research, ExoPlayer does not
                // currently support m3u playlists. If I had more time, I would dig
                // into this further and find a workaround to play the media (such
                // as suggestion in https://github.com/google/ExoPlayer/issues/4066),
                // but for now, here's a google API MP3 to listen to in the meantime!
                val mediaItem = MediaItem.fromUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
                // val mediaItem = MediaItem.fromUri(mediaUrl!!)
                exoPlayer.setMediaItem(mediaItem)

                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentWindowIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }
}