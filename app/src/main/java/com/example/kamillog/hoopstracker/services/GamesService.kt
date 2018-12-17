package com.example.kamillog.hoopstracker.services

import com.example.kamillog.hoopstracker.models.GameItem

class GamesService {
    companion object {
        var finishedGames: List<GameItem> = listOf()
        var upcomingGames: List<GameItem> = listOf()
    }
}