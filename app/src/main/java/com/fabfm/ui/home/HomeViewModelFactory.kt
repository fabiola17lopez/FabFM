package com.fabfm.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import service.RadioTimeService

class HomeViewModelFactory(private val radioTimeService: RadioTimeService): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(RadioTimeService::class.java).newInstance(radioTimeService)
    }
}