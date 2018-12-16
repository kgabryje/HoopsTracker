package com.example.kamillog.hoopstracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.viewholders.GameViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val mDatabaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("games")
    private val options: FirebaseRecyclerOptions<GameItem> =
        FirebaseRecyclerOptions.Builder<GameItem>()
            .setQuery(mDatabaseReference, GameItem::class.java)
            .build()

    private val mAdapter: FirebaseRecyclerAdapter<GameItem, GameViewHolder> =
        object: FirebaseRecyclerAdapter<GameItem, GameViewHolder>(options) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.game_item,parent,false)
                return GameViewHolder(view)
            }


            override fun onBindViewHolder(holder: GameViewHolder?, position: Int,
                                          game: GameItem) {
                holder?.run {
                    game.homeTeam.logo?.let { setHomeTeam(this@HomeActivity, it) }
                    game.awayTeam.logo?.let { setAwayTeam(this@HomeActivity, it) }
//                    setScore(game.homeTeamScore, game.awayTeamScore)
                    setGameDate(game.date)
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
        setContentView(R.layout.activity_home)

        homeRecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext,DividerItemDecoration.VERTICAL))
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
