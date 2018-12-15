package com.example.kamillog.hoopstracker.models

data class UserModel(
    val email: String = "",
    val followedTeams: MutableList<TeamItem> = mutableListOf()
)