package com.example.kamillog.hoopstracker.models

import java.time.ZonedDateTime

data class GameItem(
    val id: String = "",
    val date: ZonedDateTime = ZonedDateTime.now(),
    val homeTeam: TeamItem = TeamItem(),
    val awayTeam: TeamItem = TeamItem(),
    val homeTeamScore: String = "",
    val awayTeamScore: String = ""
)