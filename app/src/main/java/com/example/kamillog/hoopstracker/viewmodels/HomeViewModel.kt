package com.example.kamillog.hoopstracker.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.models.ScheduleEndpoint
import com.example.kamillog.hoopstracker.models.TeamGameLogsEndpoint
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.services.MySportsFeedsApi
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.utils.Converter
import com.google.firebase.database.*
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class HomeViewModel : ViewModel() {
    private val API_KEY = "0c9e9ab5-0827-4ae3-95f2-6bb541"
    private val password = "mikaella123"
    private val encoding = Base64.getEncoder().encodeToString("$API_KEY:$password".toByteArray())
    private val baseUrl = "https://api.mysportsfeeds.com/v1.2/pull/nba/"

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("followedTeams")
        .child(LoginService.mAuth.uid!!)

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

    private val finishedGamesLiveData = MutableLiveData<List<GameItem>>()
    private val upcomingGamesLiveData = MutableLiveData<List<GameItem>>()
    private val followedTeamsLiveData = MutableLiveData<List<TeamItem>>()

    fun finishedGames(): LiveData<List<GameItem>> = finishedGamesLiveData
    fun upcomingGames(): LiveData<List<GameItem>> = upcomingGamesLiveData
    fun followedTeams(): LiveData<List<TeamItem>> = followedTeamsLiveData

    fun getTeamLogs(teams: List<TeamItem>) {
        val teamsString = teams.joinToString(",", transform = { teamItem ->
            "${teamItem.city.replace(" ", "")}-${teamItem.name.replace(" ", "")}"
        })
        val call = apiConnector.getTeamGameLogs("latest", teamsString, "since-3-days-ago")
        call.enqueue(object : Callback<TeamGameLogsEndpoint> {
            override fun onResponse(call: Call<TeamGameLogsEndpoint>, response: Response<TeamGameLogsEndpoint>) {
                val res = response.body()?.teamgamelogs?.gamelogs?.map {
                    Converter().gameLogToGameItem(it)
                }
                if (res != null) {
                    finishedGamesLiveData.value = res
                }
            }

            override fun onFailure(call: Call<TeamGameLogsEndpoint>, t: Throwable) {

            }
        })
    }

    fun getUpcomingGames(teams: List<TeamItem>) {
        val teamsString = teams.joinToString(",", transform = { teamItem ->
            "${teamItem.city.replace("\\s", "")}-${teamItem.name.replace("\\s", "")}"
        })
        val call = apiConnector.getSchedule("latest", teamsString, "from-today-to-7-days-from-now")
        call.enqueue(object : Callback<ScheduleEndpoint> {
            override fun onResponse(call: Call<ScheduleEndpoint>, response: Response<ScheduleEndpoint>) {
                val res = response.body()?.fullgameschedule?.gameentry?.map {
                    Converter().gameApiToGameItem(it)
                }
                if (res != null) {
                    upcomingGamesLiveData.value = res
                }
            }

            override fun onFailure(call: Call<ScheduleEndpoint>, t: Throwable) {
                TODO("not implemented")
            }
        })
    }

    fun loadFollowedTeams(reload: Boolean = false) {
        if (TeamsService.followedTeams.size > 0 && !reload) {
            followedTeamsLiveData.value = TeamsService.followedTeams
            return
        }
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    return
                }
                val teams = mutableListOf<TeamItem>()
                for (teamJson in snapshot.children) {
                    val backgroundLogo = teamJson.child("backgroundLogo").value as String
                    val logo = teamJson.child("logo").value as String
                    val city = teamJson.child("city").value as String
                    val name = teamJson.child("name").value as String
                    teams.add(TeamItem(city, name, logo, backgroundLogo))
                }
                TeamsService.followedTeams = teams
                followedTeamsLiveData.value = TeamsService.followedTeams
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }
        })

    }
}