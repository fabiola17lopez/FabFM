package model

sealed class RadioTimeState {
    data class Success(
        val elements: List<RadioTimeElement>?,
        val title: String? = null,
    ): RadioTimeState()

    object Error: RadioTimeState()
}
