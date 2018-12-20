package com.example.kamillog.hoopstracker.services

import android.content.Context
import android.widget.ImageView
import com.example.kamillog.hoopstracker.models.TeamItem
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
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


        fun loadTeams(context: Context? = null) {
            mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val teams = mutableListOf<TeamItem>()
                    for (teamJson in snapshot.children) {
                        val backgroundLogo = teamJson.child("backgroundLogo").value as String
                        val logo = teamJson.child("logo").value as String
                        val city = teamJson.child("city").value as String
                        val name = teamJson.child("name").value as String
                        teams.add(TeamItem(city, name, logo, backgroundLogo))
                        if (context != null) {
                            val img = ImageView(context)
                            Picasso.with(context).load(backgroundLogo).into(img)
                        }
                    }
                    TeamsService.teams = teams
                }

                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }
    }
}