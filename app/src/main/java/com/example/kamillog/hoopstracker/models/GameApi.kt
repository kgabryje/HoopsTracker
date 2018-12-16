package com.example.kamillog.hoopstracker.models

data class GameApi(
    val id: String = "",
    val date: String = "",
    val time: String = "",
    val awayTeam: TeamApi = TeamApi(),
    val homeTeam: TeamApi = TeamApi()
)