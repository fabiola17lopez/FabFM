package model

sealed class RadioTimeElement {
    data class Link(
        val url: String,
        val text: String? = null,
        val image: String? = null,
    ): RadioTimeElement()

    data class Audio(
        val url: String,
        val text: String? = null,
        val subtext: String? = null,
        val image: String? = null,
    ): RadioTimeElement()

    data class Section(
        val text: String?,
        val elements: List<RadioTimeElement>,
    ): RadioTimeElement()
}
