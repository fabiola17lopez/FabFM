package service

import io.reactivex.Single
import model.RadioTimeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RadioTimeApi {

    @GET("/")
    fun getHierarchy(@Query("render") render: String?): Single<RadioTimeResponse>

    @GET("/Browse.ashx")
    fun getLink(
        @Query("render") render: String?,
        @Query("id") id: String? = "c1390807",
    ): Single<RadioTimeResponse>
}