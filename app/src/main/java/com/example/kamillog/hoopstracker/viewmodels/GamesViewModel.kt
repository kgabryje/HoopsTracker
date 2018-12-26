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

    fun getTeamLogs(teams: List<TeamItem>, reload: Boolean = false, isSingleTeamView: Boolean = false) {
        if (GamesService.finishedGames.isNotEmpty() && !reload) {
            if (isSingleTeamView) {
                finishedGamesLiveData.value = GamesService.finishedGames.filter {
                    it.homeTeam == teams[0] || it.awayTeam == teams[0]
                }
            } else {
                finishedGamesLiveData.value = GamesService.finishedGames
            }
            return
        }

        if (teams.isEmpty()) {
            finishedGamesLiveData.value = listOf()
            return
        }
        val teamsString = teams.joinToString(",", transform = { teamItem ->
            "${teamItem.city.replace(" ", "")}-${teamItem.name.replace(" ", "")}"
        })

        val call = apiConnector.getTeamGameLogs("latest", teamsString, "since-7-days-ago")
        call.enqueue(object : Callback<TeamGameLogsEndpoint> {
            override fun onResponse(call: Call<TeamGameLogsEndpoint>, response: Response<TeamGameLogsEndpoint>) {
                if (response.isSuccessful) {
                    val gameItems = response.body()?.teamgamelogs?.gamelogs?.map {
                        Converter().gameLogToGameItem(it)
                    }
                    if (gameItems != null) {
                        finishedGamesLiveData.value = gameItems.distinct().sortedByDescending { it.date }
                        if (!isSingleTeamView) GamesService.finishedGames = finishedGamesLiveData.value!!
                    }
                } else {
                    finishedGamesLiveData.value = listOf()
                }
            }

            override fun onFailure(call: Call<TeamGameLogsEndpoint>, t: Throwable) {

            }
        })
    }

    fun getUpcomingGames(teams: List<TeamItem>, reload: Boolean = false, isSingleTeamView: Boolean = false) {
        if (GamesService.upcomingGames.isNotEmpty() && !reload) {
            if (isSingleTeamView) {
                upcomingGamesLiveData.value = GamesService.upcomingGames.filter {
                    it.awayTeam == teams[0] || it.homeTeam == teams[0]
                }
            } else {
                upcomingGamesLiveData.value = GamesService.upcomingGames
            }
            return
        }

        if (teams.isEmpty()) {
            finishedGamesLiveData.value = listOf()
            return
        }

        val teamsString = teams.joinToString(",", transform = { teamItem ->
            "${teamItem.city.replace(" ", "")}-${teamItem.name.replace(" ", "")}"
        })
        val call = apiConnector.getSchedule("latest", teamsString, "from-today-to-7-days-from-now")
        call.enqueue(object : Callback<ScheduleEndpoint> {
            override fun onResponse(call: Call<ScheduleEndpoint>, response: Response<ScheduleEndpoint>) {
                if (response.isSuccessful) {
                    val gameItems = response.body()?.fullgameschedule?.gameentry?.map {
                        Converter().gameApiToGameItem(it)
                    }
                    if (gameItems != null) {
                        upcomingGamesLiveData.value = gameItems.distinct().sortedBy { it.date }
                        if (!isSingleTeamView) GamesService.upcomingGames = upcomingGamesLiveData.value!!
                    }
                } else {
                    upcomingGamesLiveData.value = listOf()
                }
            }

            override fun onFailure(call: Call<ScheduleEndpoint>, t: Throwable) {
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
                if (snapshot.value != null) {
                    val teams = snapshot.children.mapNotNull { it.getValue(TeamItem::class.java) }.toMutableList()
                    TeamsService.followedTeams = teams
                    followedTeamsLiveData.value = TeamsService.followedTeams
                } else {
                    followedTeamsLiveData.value = listOf()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}
