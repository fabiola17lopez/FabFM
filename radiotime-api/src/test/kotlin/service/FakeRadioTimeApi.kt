package service

import io.reactivex.Single
import model.RadioTimeElementResponse
import model.RadioTimeHeadResponse
import model.RadioTimeResponse
import retrofit2.http.Query
import retrofit2.http.Url

class FakeRadioTimeApi(
    block: FakeRadioTimeApi.() -> Unit = { /* no-op default */ }
): RadioTimeApi {
    var response: RadioTimeResponse? = null

    init { block() }

    override fun getLink(
        @Url url: String,
        @Query(value = "render") render: String?
    ): Single<RadioTimeResponse> {
        return response?.let { Single.just(it) }
            ?: throw NotImplementedError()
    }

    fun responseSuccess() {
        response = RadioTimeResponse(
            head = RadioTimeHeadResponse(title = "Local Radio"),
            body = listOf(
                RadioTimeElementResponse(
                    text = "FM",
                    children = listOf(
                        RadioTimeElementResponse(
                            type = "audio",
                            text = "97.1 MyFM",
                            url = "http://my.wesite.com/audio.mp3",
                            image = "http://my.wesite.com/img.png",
                            subtext = "Today's top hits",
                        ),
                        // item with null url should not be included
                        RadioTimeElementResponse(
                            type = "audio",
                            text = "102.5 Classic Rock",
                            url = null,
                        ),
                    )
                )
            ),
        )
    }
}