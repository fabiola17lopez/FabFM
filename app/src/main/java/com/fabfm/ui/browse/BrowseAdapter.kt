package com.fabfm.ui.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fabfm.databinding.ViewElementBinding
import com.fabfm.databinding.ViewSectionHeaderBinding
import com.fabfm.ui.browse.model.BrowseElement

class BrowseAdapter(
    private var data: List<BrowseElement>,
    private val listener: (BrowseElement) -> Unit,
    ): RecyclerView.Adapter<BrowseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            BrowseViewType.TYPE_SECTION.ordinal -> {
                BrowseViewHolder.Section(ViewSectionHeaderBinding.inflate(inflater, parent, false))
            }
            BrowseViewType.TYPE_LINK.ordinal -> {
                BrowseViewHolder.Link(ViewElementBinding.inflate(inflater, parent, false))
            }
            BrowseViewType.TYPE_AUDIO.ordinal -> {
                BrowseViewHolder.Audio(ViewElementBinding.inflate(inflater, parent, false))
            }
            else -> {
                throw IllegalStateException("Unexpected view type of $viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: BrowseViewHolder, position: Int) {
        when (holder) {
            is BrowseViewHolder.Section -> holder.bind(data[position] as BrowseElement.SectionHeader)
            is BrowseViewHolder.Link -> holder.bind(data[position] as BrowseElement.Link, listener)
            is BrowseViewHolder.Audio -> holder.bind(data[position] as BrowseElement.Audio)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(data[position]) {
            is BrowseElement.SectionHeader -> BrowseViewType.TYPE_SECTION
            is BrowseElement.Link -> BrowseViewType.TYPE_LINK
            is BrowseElement.Audio -> BrowseViewType.TYPE_AUDIO
        }.ordinal
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<BrowseElement>) {
        data = newData
        notifyDataSetChanged()
    }
}

enum class BrowseViewType {
    TYPE_SECTION,
    TYPE_LINK,
    TYPE_AUDIO;
}