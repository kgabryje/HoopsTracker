package com.example.kamillog.hoopstracker.utils

import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.models.GameLog
import com.example.kamillog.hoopstracker.models.TeamApi
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.TeamsService

class Converter {
    fun gameLogToGameItem(gameLog: GameLog): GameItem {
        val homeTeam = teamApiToTeamItem(gameLog.game.homeTeam)
        val awayTeam = teamApiToTeamItem(gameLog.game.awayTeam)
        val observedTeam = teamApiToTeamItem(gameLog.team)
        var homeTeamScore: String
        var awayTeamScore: String
        if (observedTeam == homeTeam) {
            homeTeamScore = gameLog.stats.Pts.points
            awayTeamScore = gameLog.stats.PtsAgainst.points
        } else {
            homeTeamScore = gameLog.stats.PtsAgainst.points
            awayTeamScore = gameLog.stats.Pts.points
        }
        return GameItem(gameLog.game.id, gameLog.game.date, gameLog.game.time, homeTeam, awayTeam, homeTeamScore, awayTeamScore)
    }

    fun teamApiToTeamItem(teamApi: TeamApi): TeamItem {
        return TeamsService.teams.find { teamItem -> teamItem.name == teamApi.Name }!!
    }
}