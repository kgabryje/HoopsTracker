package com.example.kamillog.hoopstracker.models

data class GameItem(
    val date: String = "",
    val homeTeam: String = "",
    val homeTeamLogo: String? = null,
    val awayTeam: String = "",
    val awayTeamLogo: String? = null,
    val homeTeamScore: Int? = 0,
    val awayTeamScore: Int? = 0
)