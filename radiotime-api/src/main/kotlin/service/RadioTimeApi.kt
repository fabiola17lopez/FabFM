package service

import io.reactivex.Single
import model.RadioTimeResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface RadioTimeApi {

    @GET
    fun getLink(
        @Url url: String,
        @Query("render") render: String?,
    ): Single<RadioTimeResponse>
}