package com.example.kamillog.hoopstracker.models

data class GameItem(
    val date: String = "",
    val homeTeam: TeamItem = TeamItem(),
    val awayTeam: TeamItem = TeamItem(),
    val homeTeamScore: Int? = 0,
    val awayTeamScore: Int? = 0
)