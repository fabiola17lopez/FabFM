package com.fabfm.ui.home

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.fabfm.R
import com.fabfm.databinding.ViewElementBinding
import com.fabfm.databinding.ViewSectionHeaderBinding
import com.fabfm.ui.home.model.BrowseElement

private const val ROUNDED_CORNER_RADIUS = 8
sealed class BrowseViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    data class Section(val binding: ViewSectionHeaderBinding): BrowseViewHolder(binding.root) {
        fun bind(element: BrowseElement.SectionHeader) {
            binding.sectionHeader.text = element.text
        }
    }

    data class Link(val binding: ViewElementBinding): BrowseViewHolder(binding.root) {
        fun bind(element: BrowseElement.Link) {
            binding.apply {
                root.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.lighter_gray))
                text.text = element.text
                subtext.isVisible = element.subtext != null
                element.subtext?.let { subtext.text = it }
                element.image?.let { loadImage(image, it) }
                image.isVisible = element.image != null
            }
        }
    }

    data class Audio(val binding: ViewElementBinding): BrowseViewHolder(binding.root) {

        fun bind(element: BrowseElement.Audio) {
            binding.apply {
                root.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.light_gray))
                text.text = element.text
                subtext.isVisible = element.subtext != null
                element.subtext?.let { subtext.text = it }
                element.image?.let { loadImage(image, it) }
                image.isVisible = element.image != null
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