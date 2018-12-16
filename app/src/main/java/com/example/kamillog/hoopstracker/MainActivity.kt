package com.example.kamillog.hoopstracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.TeamsService
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private val mDatabaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("teams")

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)

        loadTeams()

        val mainIntent = Intent(this, LoginActivity::class.java)
        startActivity(mainIntent)
        finish()
    }

    fun loadTeams() {
        mDatabaseReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val teams = mutableListOf<TeamItem>()
                for (teamJson in snapshot.children) {
                    val backgroundLogo = teamJson.child("backgroundLogo").value as String
                    val logo = teamJson.child("logo").value as String
                    val city = teamJson.child("city").value as String
                    val name = teamJson.child("name").value as String
                    teams.add(TeamItem(city, name, logo, backgroundLogo))
                }
                TeamsService.teams = teams
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}
