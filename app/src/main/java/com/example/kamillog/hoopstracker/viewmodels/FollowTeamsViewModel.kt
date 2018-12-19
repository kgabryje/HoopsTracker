package com.example.kamillog.hoopstracker.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.utils.ToastMessageHandler
import com.google.firebase.database.*

class FollowTeamsViewModel(context: Context) : ViewModel() {
    private val toastMessageHandler = ToastMessageHandler(context)

    private val followedTeamsLiveData = MutableLiveData<MutableList<TeamItem>>()
    fun followedTeams(): LiveData<MutableList<TeamItem>> = followedTeamsLiveData

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
        .child("followedTeams")
        .child(LoginService.mAuth.uid!!)

    init {
        followedTeamsLiveData.value = mutableListOf()
    }

    fun loadFollowedTeams(reload: Boolean = false) {
        if (TeamsService.followedTeams.size > 0 && !reload) {
            followedTeamsLiveData.value = TeamsService.followedTeams
            return
        }

        dbRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    return
                }
                val teamList = (snapshot.value as ArrayList<*>).map {
                    val team = it as Map<String, String>
                    TeamItem(team["city"]!!, team["name"]!!, team["logo"]!!, team["backgroundLogo"]!!)
                }.toMutableList()
                followedTeamsLiveData.value = teamList
                TeamsService.followedTeams = teamList
            }
        })
    }

    fun saveFollowedTeams() {
        dbRef.setValue(followedTeamsLiveData.value)
        dbRef.push()
    }

    fun toggleTeam(team: TeamItem) {
        if (followedTeamsLiveData.value!!.contains(team)) {
            followedTeamsLiveData.value!!.remove(team)
            toastMessageHandler.showToastMessage("${team.city} ${team.name} removed from tracked teams")
        } else {
            followedTeamsLiveData.value!!.add(team)
            toastMessageHandler.showToastMessage("${team.city} ${team.name} added to tracked teams")
        }
        TeamsService.followedTeams = followedTeamsLiveData.value!!
        followedTeamsLiveData.value = followedTeamsLiveData.value
        saveFollowedTeams()
    }
}