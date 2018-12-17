package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kamillog.hoopstracker.adapters.TeamsAdapter
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.RestApiConnector
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.viewholders.TeamsViewHolder
import com.example.kamillog.hoopstracker.viewmodels.FollowTeamsViewModel
import com.example.kamillog.hoopstracker.viewmodels.ViewModelFactory
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_follow_teams.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.team_item.view.*

class FollowTeamsActivity : AppCompatActivity() {
    private lateinit var viewModel: FollowTeamsViewModel

    private val teamsAdapter = TeamsAdapter(this, TeamsService.teams, ::toggleTeam)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_teams)
        setSupportActionBar(toolbar)

        followTeamsRecyclerView.apply {
            adapter = teamsAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        }

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this, "FollowTeams"))
            .get(FollowTeamsViewModel::class.java)

        viewModel.followedTeams().observe(this, Observer {
            teamsAdapter.notifyDataSetChanged()
        })

        viewModel.loadFollowedTeams()
    }

    fun toggleTeam(teamItem: TeamItem) {
        viewModel.toggleTeam(teamItem)
    }

}
