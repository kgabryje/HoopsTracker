package com.example.kamillog.hoopstracker.fragments

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.models.ScheduleEndpoint
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.*
import com.example.kamillog.hoopstracker.utils.Converter
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingGamesViewModel : ViewModel() {
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("followedTeams")
        .child(LoginService.mAuth.uid!!)

    private val apiConnector: MySportsFeedsApi = RestApiConnector().apiConnector()

    private val upcomingGamesLiveData = MutableLiveData<List<GameItem>>()
    private val followedTeamsLiveData = MutableLiveData<List<TeamItem>>()

    fun upcomingGames(): LiveData<List<GameItem>> = upcomingGamesLiveData
    fun followedTeams(): LiveData<List<TeamItem>> = followedTeamsLiveData

    fun getUpcomingGames(teams: List<TeamItem>, reload: Boolean = false) {
        if (GamesService.upcomingGames.isNotEmpty() && !reload) {
            upcomingGamesLiveData.value = GamesService.upcomingGames
            return
        }

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
