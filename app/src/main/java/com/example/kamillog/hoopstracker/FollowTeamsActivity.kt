package com.example.kamillog.hoopstracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.viewholders.TeamsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_follow_teams.*

class FollowTeamsActivity : AppCompatActivity() {

    private val mDatabaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("teams")
    private val options: FirebaseRecyclerOptions<TeamItem> =
        FirebaseRecyclerOptions.Builder<TeamItem>()
            .setQuery(mDatabaseReference, TeamItem::class.java)
            .build()

    private val mAdapter: FirebaseRecyclerAdapter<TeamItem, TeamsViewHolder> =
        object: FirebaseRecyclerAdapter<TeamItem, TeamsViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.team_item,parent,false)
                return TeamsViewHolder(view)
            }


            override fun onBindViewHolder(holder: TeamsViewHolder?, position: Int,
                                          team: TeamItem
            ) {
                holder?.run {
                    team.backgroundLogo?.let { setTeamLogo(this@FollowTeamsActivity, it) }
//                    itemView.setOnClickListener {
//                        startActivity(
//                            Intent(activity, ExerciseDetailsActivity::class.java)
//                                .putExtra("title", game?.title)
//                        )
//                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_teams)

        followTeamsRecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        }
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
}
