package com.example.coronaupdate.retrofit

import com.example.coronaupdate.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private val baseURL = "https://covid-19-coronavirus-statistics.p.rapidapi.com/"
    private var retrofit: Retrofit? = null
    private val REQUEST_TIMEOUT = 60
    private var logging = HttpLoggingInterceptor()

    private fun getOkHttpLogClient(): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)


        //Adding http logging
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        }
        httpClient.addInterceptor(logging)

        //Adding headers
        httpClient.addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("X-RapidAPI-Key","a12ef4c3cdmsh2c8b16201701334p19d554jsna37eb9c0611d")
                .addHeader("X-RapidAPI-Host", "covid-19-coronavirus-statistics.p.rapidapi.com")
                .build()
            chain.proceed(newRequest)
        }

        return httpClient.build()
    }

    //Get Retrofit Instance
    fun getClient(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpLogClient())
                .build()
        }
        return retrofit
    }
}