package com.example.kamillog.hoopstracker.utils

import com.example.kamillog.hoopstracker.models.*
import com.example.kamillog.hoopstracker.services.TeamsService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Converter {
    fun gameLogToGameItem(gameLog: GameLog): GameItem {
        val homeTeam = teamApiToTeamItem(gameLog.game.homeTeam)
        val awayTeam = teamApiToTeamItem(gameLog.game.awayTeam)
        val observedTeam = teamApiToTeamItem(gameLog.team)
        val homeTeamScore: String
        val awayTeamScore: String
        if (observedTeam == homeTeam) {
            homeTeamScore = gameLog.stats.Pts.points
            awayTeamScore = gameLog.stats.PtsAgainst.points
        } else {
            homeTeamScore = gameLog.stats.PtsAgainst.points
            awayTeamScore = gameLog.stats.Pts.points
        }
        val date = LocalDateTime.parse(
            "${gameLog.game.date} ${gameLog.game.time}",
            DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
        )
        return GameItem(gameLog.game.id, date, homeTeam, awayTeam, homeTeamScore, awayTeamScore)
    }

    fun gameApiToGameItem(gameApi: GameApi): GameItem {
        val homeTeam = teamApiToTeamItem(gameApi.homeTeam)
        val awayTeam = teamApiToTeamItem(gameApi.awayTeam)
        val date = LocalDateTime.parse(
            "${gameApi.date} ${gameApi.time}",
            DateTimeFormatter.ofPattern("yyyy-MM-dd h:mma", Locale.ENGLISH)
        )
        return GameItem(gameApi.id, date, homeTeam, awayTeam)
    }

    fun teamApiToTeamItem(teamApi: TeamApi): TeamItem {
        return TeamsService.teams.find { teamItem -> teamItem.name == teamApi.Name }!!
    }
}