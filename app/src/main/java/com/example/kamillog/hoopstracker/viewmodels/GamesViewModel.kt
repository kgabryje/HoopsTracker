package com.example.kamillog.hoopstracker.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.models.ScheduleEndpoint
import com.example.kamillog.hoopstracker.models.TeamGameLogsEndpoint
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.*
import com.example.kamillog.hoopstracker.utils.Converter
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesViewModel : ViewModel() {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("followedTeams")
        .child(LoginService.mAuth.uid!!)

    private val apiConnector: MySportsFeedsApi = RestApiConnector().apiConnector()

    private val finishedGamesLiveData = MutableLiveData<List<GameItem>>()
    private val upcomingGamesLiveData = MutableLiveData<List<GameItem>>()
    private val followedTeamsLiveData = MutableLiveData<List<TeamItem>>()

    fun finishedGames(): LiveData<List<GameItem>> = finishedGamesLiveData
    fun upcomingGames(): LiveData<List<GameItem>> = upcomingGamesLiveData
    fun followedTeams(): LiveData<List<TeamItem>> = followedTeamsLiveData

    fun getTeamLogs(teams: List<TeamItem>, reload: Boolean = false) {
        if (GamesService.finishedGames.isNotEmpty() && !reload) {
            finishedGamesLiveData.value = GamesService.finishedGames
            return
        }

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
                    GamesService.finishedGames = res.distinct().sortedByDescending { it.date }
                    finishedGamesLiveData.value = GamesService.finishedGames
                }
            }

            override fun onFailure(call: Call<TeamGameLogsEndpoint>, t: Throwable) {

            }
        })
    }

    fun getUpcomingGames(teams: List<TeamItem>, reload: Boolean = false) {
        if (GamesService.upcomingGames.isNotEmpty() && !reload) {
            upcomingGamesLiveData.value = GamesService.upcomingGames
            return
        }

        val teamsString = teams.joinToString(",", transform = { teamItem ->
            "${teamItem.city.replace(" ", "")}-${teamItem.name.replace(" ", "")}"
        })
        val call = apiConnector.getSchedule("latest", teamsString, "from-today-to-7-days-from-now")
        call.enqueue(object : Callback<ScheduleEndpoint> {
            override fun onResponse(call: Call<ScheduleEndpoint>, response: Response<ScheduleEndpoint>) {
                val res = response.body()?.fullgameschedule?.gameentry?.map {
                    Converter().gameApiToGameItem(it)
                }
                if (res != null) {
                    GamesService.upcomingGames = res.distinct()
                    upcomingGamesLiveData.value = GamesService.upcomingGames
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
                    TeamsService.followedTeams = mutableListOf()
                    followedTeamsLiveData.value = TeamsService.followedTeams
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
