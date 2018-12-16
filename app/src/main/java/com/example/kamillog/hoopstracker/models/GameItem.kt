package com.example.kamillog.hoopstracker.models

import java.time.LocalDateTime

data class GameItem(
    val id: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
    val homeTeam: TeamItem = TeamItem(),
    val awayTeam: TeamItem = TeamItem(),
    val homeTeamScore: String? = "",
    val awayTeamScore: String? = ""
)