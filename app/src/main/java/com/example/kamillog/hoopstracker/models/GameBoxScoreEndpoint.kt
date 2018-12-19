package com.example.kamillog.hoopstracker.models

import com.google.gson.annotations.SerializedName

data class GameBoxScoreEndpoint(
    @SerializedName("gameboxscore") val gameBoxScore: GameBoxScoreItem = GameBoxScoreItem()
)