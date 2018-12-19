package com.example.kamillog.hoopstracker.models

import com.google.gson.annotations.SerializedName

data class QuarterItem(
    @SerializedName("@number") val number: String = "",
    val awayScore: String = "",
    val homeScore: String = ""
)