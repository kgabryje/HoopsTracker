package com.example.kamillog.hoopstracker.models

import com.google.gson.annotations.SerializedName

data class PointsApi(
    @SerializedName("#text") val points: String = ""
)