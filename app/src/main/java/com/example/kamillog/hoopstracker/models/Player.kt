package com.example.kamillog.hoopstracker.models

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("ID") val id: String = "",
    @SerializedName("LastName") val lastName: String = "",
    @SerializedName("FirstName") val firstName: String = "",
    @SerializedName("Position") val position: String = ""
)