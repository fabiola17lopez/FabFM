package com.fabfm.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fabfm.MainActivity
import com.fabfm.R
import com.fabfm.databinding.FragmentBrowseBinding
import com.fabfm.ui.browse.model.BrowseElement
import com.fabfm.ui.browse.model.BrowseState
import com.fabfm.ui.play.PlayerBottomSheetFragment
import io.reactivex.disposables.CompositeDisposable
import model.RadioTimeTransformer
import service.RadioTimeServiceImpl
import service.getRadioTimeApi

private const val ARG_FETCH_DATA_URL = "FetchDataUrl"
class BrowseFragment : Fragment() {

    companion object {
        fun newInstance(url: String): BrowseFragment =
            BrowseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FETCH_DATA_URL, url)
                }
            }

        val TAG = "HOME_FRAGMENT"
    }

    private lateinit var browseViewModel: BrowseViewModel
    private var _binding: FragmentBrowseBinding? = null
    private lateinit var browseAdapter: BrowseAdapter

    private var baseUrl: String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseUrl = arguments?.getString(ARG_FETCH_DATA_URL)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // I would normally likely set up dependency injection rather than
        // initializing these values here
        val radioTimeService = RadioTimeServiceImpl(getRadioTimeApi(), RadioTimeTransformer())
        browseViewModel = ViewModelProvider(this, BrowseViewModelFactory(radioTimeService))
            .get(BrowseViewModel::class.java)

        _binding = FragmentBrowseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setUpAdapter()
        return root
    }

    private fun setUpAdapter() {
        browseAdapter = BrowseAdapter(emptyList()) {
            when (it) {
                is BrowseElement.Audio -> navigateToPlayerFragment(it.url)
                is BrowseElement.Link -> navigateToBrowseFragment(it.url)
            }

        }
        binding.elementList.apply {
            adapter = browseAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable.add(
            browseViewModel.state()
                .subscribe { state ->
                    when (state) {
                        is BrowseState.Success -> renderSuccess(state)
                        BrowseState.Empty -> renderEmptyState()
                        BrowseState.Error -> showError()
                        BrowseState.Loading -> renderLoadingState()
                    }
                }
        )

        browseViewModel.loadData(baseUrl!!)
    }

    private fun navigateToBrowseFragment(url: String) {
        if (isAdded) {
            val fragment = newInstance(url)
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_main, fragment, TAG)
                .commit()
        }
    }

    private fun navigateToPlayerFragment(url: String) {
        PlayerBottomSheetFragment.newInstance(url)
            .show(parentFragmentManager, TAG)
    }

    private fun renderSuccess(state: BrowseState.Success) {
        hideLoadingSpinner()
        (activity as MainActivity).setActionBarTitle(state.title.orEmpty())
        browseAdapter.updateData(state.elements)
    }

    private fun renderLoadingState() {
        showLoadingSpinner()
    }

    private fun showError() {
        hideLoadingSpinner()
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(R.string.error_message)
            .setPositiveButton(R.string.try_again) { _, _ ->
                showLoadingSpinner()
                browseViewModel.loadData(baseUrl!!)
            }
            .show()
        renderEmptyState()
    }

    private fun renderEmptyState() {
        hideLoadingSpinner()
        browseAdapter.updateData(emptyList())
    }

    private fun showLoadingSpinner() {
        binding.loading.isVisible = true
    }

    private fun hideLoadingSpinner() {
        binding.loading.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}