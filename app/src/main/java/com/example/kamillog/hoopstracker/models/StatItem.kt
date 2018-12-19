package com.example.kamillog.hoopstracker.models

import com.google.gson.annotations.SerializedName

data class StatItem(
    @SerializedName("@category") val category: String = "",
    @SerializedName("@abbreviation") val abbreviation: String = "",
    @SerializedName("#text") val value: String = ""
)