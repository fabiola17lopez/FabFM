package service

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import model.RadioTimeResponse
import model.RadioTimeState
import model.RadioTimeTransformer

private const val RENDER_JSON = "json"
class RadioTimeServiceImpl(
    private val radioTimeApi: RadioTimeApi,
    private val radioTimeTransformer: RadioTimeTransformer,
): RadioTimeService {
    override fun getRadioTimeData(url: String) : Single<RadioTimeState> {
        return radioTimeApi.getLink(url = url, render = RENDER_JSON)
            .subscribeOn(Schedulers.io())
            .map { radioTimeTransformer.transform(it) }
            .onErrorReturn { RadioTimeState.Error }
    }
}