package service

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import model.RadioTimeResponse
import model.RadioTimeState
import model.RadioTimeTransformer

interface RadioTimeService {
    fun getBaseHierarchy() : Single<RadioTimeState>
}