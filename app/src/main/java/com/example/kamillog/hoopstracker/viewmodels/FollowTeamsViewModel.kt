package com.example.kamillog.hoopstracker.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.services.TeamsService
import com.google.firebase.database.*

class FollowTeamsViewModel : ViewModel() {
    private val followedTeamsLiveData = MutableLiveData<MutableList<TeamItem>>()
    private val toastMessagesLiveData = MutableLiveData<String>()

    fun followedTeams(): LiveData<MutableList<TeamItem>> = followedTeamsLiveData
    fun messages(): LiveData<String> = toastMessagesLiveData

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
                val teamList = snapshot.children.mapNotNull { it.getValue(TeamItem::class.java) }.toMutableList()
                followedTeamsLiveData.value = teamList
                TeamsService.followedTeams = teamList
            }
        })
    }

    private fun saveFollowedTeams() {
        dbRef.setValue(followedTeamsLiveData.value)
        dbRef.push()
    }

    fun toggleTeam(team: TeamItem) {
        followedTeamsLiveData.value?.let {
            if (it.contains(team)) {
                it.remove(team)
                toastMessagesLiveData.value = "${team.city} ${team.name} removed from tracked teams"
            } else {
                it.add(team)
                toastMessagesLiveData.value = "${team.city} ${team.name} added to tracked teams"
            }
            TeamsService.followedTeamsChanged = true
            TeamsService.followedTeams = it
            followedTeamsLiveData.value = it
            saveFollowedTeams()
        }

    }
}