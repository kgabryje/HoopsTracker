package com.example.kamillog.hoopstracker.models

data class GameLog(
    val game: GameApi = GameApi(),
    val stats: PointsItem = PointsItem(),
    val team: TeamApi = TeamApi()
)