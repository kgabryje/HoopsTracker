package com.example.kamillog.hoopstracker.services

import com.example.kamillog.hoopstracker.models.TeamItem

class TeamsService {
    companion object {
        var teams: MutableList<TeamItem> = mutableListOf()
        var followedTeams: MutableList<TeamItem> = mutableListOf()
    }
}