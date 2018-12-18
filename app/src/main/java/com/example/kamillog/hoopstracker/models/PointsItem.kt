package com.example.kamillog.hoopstracker.models

data class PointsItem(
    val Pts: PointsApi = PointsApi(),
    val PtsAgainst: PointsApi = PointsApi()
)