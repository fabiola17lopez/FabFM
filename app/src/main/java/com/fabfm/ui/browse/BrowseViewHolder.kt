package com.fabfm.ui.browse

import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fabfm.databinding.ViewElementBinding
import com.fabfm.databinding.ViewSectionHeaderBinding
import com.fabfm.ui.browse.model.BrowseElement

private const val ROUNDED_CORNER_RADIUS = 8
sealed class BrowseViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    data class Section(val binding: ViewSectionHeaderBinding): BrowseViewHolder(binding.root) {
        fun bind(element: BrowseElement.SectionHeader) {
            binding.sectionHeader.text = element.text
        }
    }

    data class Link(val binding: ViewElementBinding): BrowseViewHolder(binding.root) {
        fun bind(element: BrowseElement.Link, listener: (BrowseElement) -> Unit) {
            binding.apply {
                text.text = element.text
                subtext.isVisible = element.subtext != null
                element.subtext?.let { subtext.text = it }
                element.image?.let { loadImage(image, it) }
                image.isVisible = element.image != null

                root.cardElevation = 0f
                root.setOnClickListener { listener(element) }
            }
        }
    }

    data class Audio(val binding: ViewElementBinding): BrowseViewHolder(binding.root) {

        fun bind(element: BrowseElement.Audio, listener: (BrowseElement) -> Unit) {
            binding.apply {
                text.text = element.text
                subtext.isVisible = element.subtext != null
                element.subtext?.let { subtext.text = it }
                element.image?.let { loadImage(image, it) }
                image.isVisible = element.image != null

                root.cardElevation = 20f
                root.setOnClickListener { listener(element) }
            }
        }
    }

    fun loadImage(imageView: ImageView, url: String) {
        imageView.isVisible = true
        Glide.with(view.context)
            .load(url)
            .fitCenter()
            .transform(RoundedCorners(ROUNDED_CORNER_RADIUS))
            .into(imageView)
    }
}