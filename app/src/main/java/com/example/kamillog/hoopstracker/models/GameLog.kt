package com.example.kamillog.hoopstracker.models

data class GameLog(
    val game: GameApi = GameApi(),
    val stats: StatsItem = StatsItem(),
    val team: TeamApi = TeamApi()
)