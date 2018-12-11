package com.example.kamillog.hoopstracker.models

import java.util.*

data class GameItem(
    val id: Int,
    val date: Date,
    val homeTeam: TeamItem,
    val awayTeam: TeamItem,
    val homeTeamScore: Int?,
    val awayTeamScore: Int?
)