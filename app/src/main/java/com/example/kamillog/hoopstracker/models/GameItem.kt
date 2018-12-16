package com.example.kamillog.hoopstracker.models

data class GameItem(
    val id: String = "",
    val date: String = "",
    val time: String = "",
    val homeTeam: TeamItem = TeamItem(),
    val awayTeam: TeamItem = TeamItem(),
    val homeTeamScore: String? = "",
    val awayTeamScore: String? = ""
)