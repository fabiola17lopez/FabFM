package com.fabfm.ui.browse.model

import model.RadioTimeElement

sealed class BrowseElement {
    data class Link(
        val url: String,
        val text: String? = null,
        val subtext: String? = null,
        val image: String? = null,
    ) : BrowseElement()

    data class Audio(
        val url: String,
        val text: String? = null,
        val subtext: String? = null,
        val image: String? = null,
    ) : BrowseElement()

    data class SectionHeader(
        val text: String?,
    ) : BrowseElement()

    companion object {
        fun fromRadioTimeLink(item: RadioTimeElement.Link) =
            BrowseElement.Link(
                url = item.url,
                text = item.text,
                subtext = item.subtext,
                image = item.image,
            )

        fun fromRadioTimeAudio(item: RadioTimeElement.Audio) =
            BrowseElement.Audio(
                url = item.url,
                text = item.text,
                subtext = item.subtext,
                image = item.image,
            )
    }
}
