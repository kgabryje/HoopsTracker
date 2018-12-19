package com.example.kamillog.hoopstracker.models

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("Fg3PtAtt") val threePointAttempts: StatItem = StatItem(),
    @SerializedName("Fg3PtMade") val threePointMade: StatItem = StatItem(),
    @SerializedName("Fg3PtPct") val threePointPercent: StatItem = StatItem(),
    @SerializedName("FgAtt") val fieldGoalsAttempts: StatItem = StatItem(),
    @SerializedName("FgMade") val fieldGoalsMade: StatItem = StatItem(),
    @SerializedName("FgPct") val fieldGoalsPercent: StatItem = StatItem(),
    @SerializedName("FtAtt") val freeThrowAttempt: StatItem = StatItem(),
    @SerializedName("FtMade") val freeThrowMade: StatItem = StatItem(),
    @SerializedName("FtPct") val freeThrowPercent: StatItem = StatItem(),
    @SerializedName("Reb") val rebounds: StatItem = StatItem(),
    @SerializedName("Ast") val assists: StatItem = StatItem(),
    @SerializedName("Pst") val points: StatItem = StatItem(),
    @SerializedName("Stl") val steals: StatItem = StatItem(),
    @SerializedName("Blk") val blocks: StatItem = StatItem(),
    @SerializedName("MinSeconds") val seconds: StatItem = StatItem()
)