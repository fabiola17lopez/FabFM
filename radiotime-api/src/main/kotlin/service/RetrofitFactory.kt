package service

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val RADIOTIME_API_BASE_URL = "http://opml.radiotime.com"

fun getRadioTimeApi(): RadioTimeApi {
    val logging = HttpLoggingInterceptor()
    logging.setLevel(HttpLoggingInterceptor.Level.BODY)

    val retrofit = Retrofit.Builder()
        .baseUrl(RADIOTIME_API_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create())
        .client(OkHttpClient.Builder().addInterceptor(logging).build())
        .build()

    return retrofit.create(RadioTimeApi::class.java)
}