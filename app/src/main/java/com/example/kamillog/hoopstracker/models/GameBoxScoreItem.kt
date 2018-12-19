package com.example.kamillog.hoopstracker.models

data class GameBoxScoreItem(
    val quarterSummary: QuarterSummary = QuarterSummary(),
    val awayTeam: BoxScoreAway = BoxScoreAway(),
    val homeTeam: BoxScoreHome = BoxScoreHome()
)