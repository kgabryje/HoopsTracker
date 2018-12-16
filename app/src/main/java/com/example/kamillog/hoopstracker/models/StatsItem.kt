package com.example.kamillog.hoopstracker.models

data class StatsItem(
    val Pts: PointsApi = PointsApi(),
    val PtsAgainst: PointsApi = PointsApi()
)