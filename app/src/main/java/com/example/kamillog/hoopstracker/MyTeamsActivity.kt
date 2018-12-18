package com.example.kamillog.hoopstracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kamillog.hoopstracker.adapters.TeamsAdapter
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.viewholders.TeamsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_my_teams.*

class MyTeamsActivity : AppCompatActivity() {

    private val mAdapter = TeamsAdapter(this, TeamsService.followedTeams, ::onClick, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_teams)

        myTeamsRecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onStart() {
        super.onStart()
        mAdapter.notifyDataSetChanged()
    }

    fun onClick(team: TeamItem) {
        val intent = Intent(this, SingleTeamViewActivity::class.java).putExtra("team", team)
        startActivity(intent)
    }
}
