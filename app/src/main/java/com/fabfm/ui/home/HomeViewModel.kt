package com.fabfm.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import model.RadioTimeTransformer
import service.RadioTimeService
import service.getRadioTimeApi
import java.util.*

class HomeViewModel() : ViewModel() {

    private val radioTimeService = RadioTimeService(getRadioTimeApi(), RadioTimeTransformer())
    private val disposables = CompositeDisposable()

    private val textSubject = BehaviorSubject.create<String>()
    fun text(): Observable<String> = textSubject.hide()

    init {
        textSubject.onNext("This is home fragment")
    }

    fun loadData() {
        disposables.add(
            radioTimeService.getBaseHierarchy()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ hierarchy ->
                    textSubject.onNext(hierarchy.toString())
                }, { error ->
                    Log.e(LOAD_DATA_ERROR_TAG, error.localizedMessage ?: error.toString())
                    throw(error)
                })
        )
    }

    override fun onCleared() {
        disposables.clear()
    }

    companion object {
        const val ERROR_TAG = "HOME"
        private const val LOAD_DATA_ERROR_TAG = "$ERROR_TAG.LOAD_DATA"
    }
}