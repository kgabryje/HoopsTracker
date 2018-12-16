package com.example.kamillog.hoopstracker.services

import com.example.kamillog.hoopstracker.models.TeamGameLogsEndpoint
import retrofit2.Callback
import retrofit2.Retrofit
import java.util.*
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Response


class RestApiConnector {
    private val API_KEY = "" // provide valid api key from mysportsfeeds.com for app to work
    private val password = "" // provide valid password to mysportsfeeds.com for app to work
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

    fun getTeamLogs() {
        val call = apiConnector.getTeamGameLogs("latest", "boston-celtics,gsw", "from-3-days-ago-to-today")
        call.enqueue(object : Callback<TeamGameLogsEndpoint> {
            override fun onResponse(call: Call<TeamGameLogsEndpoint>, response: Response<TeamGameLogsEndpoint>) {
                val res = response.body().toString()
            }

            override fun onFailure(call: Call<TeamGameLogsEndpoint>, t: Throwable) {

            }
        })
    }
}