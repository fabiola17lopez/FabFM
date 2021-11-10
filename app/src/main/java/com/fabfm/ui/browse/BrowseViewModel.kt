package com.fabfm.ui.browse

import android.util.Log
import androidx.lifecycle.ViewModel
import com.fabfm.ui.browse.model.BrowseState
import com.fabfm.ui.browse.model.BrowseElement
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import model.RadioTimeElement
import model.RadioTimeState
import service.RadioTimeService

class BrowseViewModel(private val radioTimeService: RadioTimeService) : ViewModel() {
    private val disposables = CompositeDisposable()

    private val stateSubject = BehaviorSubject.create<BrowseState>()
    fun state(): Observable<BrowseState> = stateSubject.hide()

    init {
        stateSubject.onNext(BrowseState.Loading)
    }

    fun loadData(url: String) {
        disposables.add(
            radioTimeService.getRadioTimeData(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    when (response) {
                        is RadioTimeState.Success -> {
                            val elements = response.elements
                            stateSubject.onNext(
                                if (elements != null && elements.isNotEmpty()) {
                                    BrowseState.Success(
                                        elements = buildContent(elements),
                                        title = response.title,
                                    )
                                } else {
                                    BrowseState.Empty
                                }
                            )
                        }
                        is RadioTimeState.Error -> {
                            stateSubject.onNext(BrowseState.Error)
                        }
                    }
                }, { error ->
                    Log.e(LOAD_DATA_ERROR_TAG, error.localizedMessage ?: error.toString())
                    throw(error)
                })
        )
    }

    private fun buildContent(response: List<RadioTimeElement>): List<BrowseElement> {
        val content = mutableListOf<BrowseElement>()
        for (item in response) {
            when (item) {
                is RadioTimeElement.Section -> {
                    content.add(BrowseElement.SectionHeader(item.text))
                    content.addAll(buildContent(item.elements))
                }
                is RadioTimeElement.Audio -> {
                    content.add(BrowseElement.fromRadioTimeAudio(item))
                }
                is RadioTimeElement.Link -> {
                    content.add(BrowseElement.fromRadioTimeLink(item))
                }
            }
        }
        return content
    }

    override fun onCleared() {
        disposables.clear()
    }

    companion object {
        const val ERROR_TAG = "BROWSE"
        private const val LOAD_DATA_ERROR_TAG = "$ERROR_TAG.LOAD_DATA"
    }
}