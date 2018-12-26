package com.example.kamillog.hoopstracker.services

import com.example.kamillog.hoopstracker.models.TeamItem
import com.google.firebase.database.*
import kotlin.properties.Delegates

class TeamsService {
    companion object {
        private val mDatabaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().reference.child("teams")
        var teams: List<TeamItem> = listOf()
        var followedTeams: MutableList<TeamItem> by Delegates.observable(
            mutableListOf()
        ) { _, _, newValue ->
            newValue.sortBy { it.city }
        }
        var followedTeamsChanged = false


        fun loadTeams() {
            mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val teams = snapshot.children.mapNotNull { it.getValue(TeamItem::class.java) }
                    TeamsService.teams = teams
                }

                override fun onCancelled(p0: DatabaseError) {
                }
            })
        }
    }
}