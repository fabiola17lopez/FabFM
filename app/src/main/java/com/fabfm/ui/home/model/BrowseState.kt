package com.fabfm.ui.home.model

sealed class BrowseState {
    data class Success(
        val elements: List<BrowseElement>,
        val title: String? = null,
    ): BrowseState()

    object Empty: BrowseState()
    object Error: BrowseState()
    object Loading: BrowseState()
}