package com.example.fabfm

import io.reactivex.Single
import model.RadioTimeElement
import model.RadioTimeState
import service.RadioTimeService

class FakeRadioTimeService(
    block: FakeRadioTimeService.() -> Unit = { /* no-op default */ }
): RadioTimeService {
    var response: RadioTimeState? = null

    init { block() }

    override fun getBaseHierarchy(): Single<RadioTimeState> {
        return response?.let { Single.just(it) }
            ?: throw NotImplementedError()
    }

    fun responseError() {
        response = RadioTimeState.Error
    }

    fun responseEmpty() {
        response = RadioTimeState.Success(
            title = "Local Radio",
            elements = listOf()
        )
    }

    fun responseSuccess() {
        response = RadioTimeState.Success(
            title = "Local Radio",
            elements = listOf(
                RadioTimeElement.Section(
                    text = "FM",
                    elements = listOf(
                        RadioTimeElement.Audio(
                            url = "http://my.wesite.com/audio.mp3",
                            text = "97.1 MyFM",
                            image = "http://my.wesite.com/img.png",
                            subtext = "Today's top hits",
                        ),
                        RadioTimeElement.Link(
                            url = "http://my.wesite.com",
                            text = "Local Radio",
                            image = "http://my.wesite.com/img2.png",
                            subtext = "Classic Rock for all ages"
                        ),
                    )
                )
            )
        )
    }
}