package com.example.kamillog.hoopstracker.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.kamillog.hoopstracker.models.BoxscoreItem
import com.example.kamillog.hoopstracker.models.GameBoxScoreEndpoint
import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.services.MySportsFeedsApi
import com.example.kamillog.hoopstracker.services.RestApiConnector
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoxscoreViewModel: ViewModel() {
    private val apiConnector: MySportsFeedsApi = RestApiConnector().apiConnector()
    private val boxscoreLiveData =  MutableLiveData<BoxscoreItem>()

    fun boxscore(): LiveData<BoxscoreItem> = boxscoreLiveData

    fun getBoxscore(game: GameItem) {
        val playerStatsString = "PTS,AST,REB,MIN,STL,BS,FGM,FGA,FG%,3PM,3PA,3P%,FTM,FTA,FT%,+/-"
        val call = apiConnector.getBoxscore("latest", game.id, "none", playerStatsString)
        call.enqueue(object: Callback<GameBoxScoreEndpoint> {
            override fun onResponse(call: Call<GameBoxScoreEndpoint>, response: Response<GameBoxScoreEndpoint>) {
                response.body()?.let {
                    val awayPlayersEntry = it.gameBoxScore.awayTeam.awayPlayers.playerEntry
                    val homePlayersEntry = it.gameBoxScore.homeTeam.homePlayers.playerEntry
                    val quarters = it.gameBoxScore.quarterSummary.quarter

                    boxscoreLiveData.value = BoxscoreItem(
                        game.homeTeam,
                        game.homeTeamScore,
                        homePlayersEntry,
                        game.awayTeam,
                        game.awayTeamScore,
                        awayPlayersEntry,
                        quarters
                    )
                }
            }

            override fun onFailure(call: Call<GameBoxScoreEndpoint>, t: Throwable) {
            }
        })
    }
}