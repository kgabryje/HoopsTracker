package com.example.kamillog.hoopstracker.models

data class BoxscoreItem(
    val homeTeam: TeamItem = TeamItem(),
    val homeScore: String = "",
    val homePlayersEntry: List<PlayerEntry> = listOf(),
    val awayTeam: TeamItem = TeamItem(),
    val awayScore: String = "",
    val awayPlayerEntry: List<PlayerEntry> = listOf(),
    val quartersItems: List<QuarterItem> = listOf()
)