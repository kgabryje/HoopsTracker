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
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.RestApiConnector
import com.example.kamillog.hoopstracker.viewholders.TeamsViewHolder
import com.example.kamillog.hoopstracker.viewmodels.FollowTeamsViewModel
import com.example.kamillog.hoopstracker.viewmodels.ViewModelFactory
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_follow_teams.*
import kotlinx.android.synthetic.main.team_item.view.*

class FollowTeamsActivity : AppCompatActivity() {
    private lateinit var viewModel: FollowTeamsViewModel

    private val mDatabaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("teams")
    private val options: FirebaseRecyclerOptions<TeamItem> =
        FirebaseRecyclerOptions.Builder<TeamItem>()
            .setQuery(mDatabaseReference, TeamItem::class.java)
            .build()

    private val mAdapter: FirebaseRecyclerAdapter<TeamItem, TeamsViewHolder> =
        object : FirebaseRecyclerAdapter<TeamItem, TeamsViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.team_item, parent, false)
                return TeamsViewHolder(view)
            }


            override fun onBindViewHolder(
                holder: TeamsViewHolder?, position: Int,
                team: TeamItem
            ) {
                holder?.run {
                    team.backgroundLogo?.let { setTeamLogo(this@FollowTeamsActivity, it) }
                    if (viewModel.followedTeams().value?.contains(team) == true) {
                        itemView.check.visibility = View.VISIBLE
                    } else {
                        itemView.check.visibility = View.GONE
                    }
                    itemView.setOnClickListener {
                        toggleTeam(team)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_teams)
        RestApiConnector().getTeamLogs()

        followTeamsRecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        }

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this, "FollowTeams"))
            .get(FollowTeamsViewModel::class.java)

        viewModel.followedTeams().observe(this, Observer { teams ->
            mAdapter.notifyDataSetChanged()
//            mAdapter.snapshots.forEachIndexed { index: Int, team: TeamItem ->
//                val vh = followTeamsRecyclerView.findViewHolderForAdapterPosition(index)
//                if (teams!!.contains(team)) {
//                    vh!!.itemView.background = getDrawable(R.drawable.team_item_border)
//                }
//            }
        })

        viewModel.loadFollowedTeams()
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onResume() {
        mAdapter.startListening()
        super.onResume()
    }

    override fun onStop() {
        mAdapter.stopListening()
        super.onStop()
    }

    fun toggleTeam(teamItem: TeamItem) {
        viewModel.toggleTeam(teamItem)
    }

}
