package com.example.kamillog.hoopstracker.services

import retrofit2.Retrofit
import java.util.*
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

class RestApiConnector {
    private val API_KEY = "0c9e9ab5-0827-4ae3-95f2-6bb541"
    private val password = "mikaella123"
    private val encoding = Base64.getEncoder().encodeToString("$API_KEY:$password".toByteArray())
    private val baseUrl = "https://api.mysportsfeeds.com/v1.2/pull/nba/"

    private val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
        val originalRequest = chain.request()
        val builder = originalRequest.newBuilder().header("Authorization", "Basic $encoding")
        val newRequest = builder.build()
        chain.proceed(newRequest)
    }.build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiConnector: MySportsFeedsApi = retrofit.create(MySportsFeedsApi::class.java)

    fun apiConnector(): MySportsFeedsApi = apiConnector
}