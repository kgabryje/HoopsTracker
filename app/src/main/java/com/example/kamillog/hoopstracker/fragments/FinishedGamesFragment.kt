package com.example.kamillog.hoopstracker.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.kamillog.hoopstracker.R
import com.example.kamillog.hoopstracker.adapters.GamesAdapter
import com.example.kamillog.hoopstracker.services.GamesService
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.viewmodels.GamesViewModel

class FinishedGamesFragment : Fragment() {

    companion object {
        fun newInstance() = FinishedGamesFragment()
    }

    private lateinit var gamesAdapter: GamesAdapter
    lateinit var viewModel: GamesViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var noTeamsFollowedTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.finished_games_fragment, container, false)
        noTeamsFollowedTextView = view.findViewById(R.id.finished_games_no_teams_followed)
        recyclerView = view.findViewById(R.id.finishedGamesRecyclerView)
        gamesAdapter = GamesAdapter(activity as Activity, GamesService.finishedGames)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = gamesAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GamesViewModel::class.java)
        viewModel.finishedGames().observe(this, Observer {
            gamesAdapter.gameList = it!!
            gamesAdapter.notifyDataSetChanged()
        })
        viewModel.followedTeams().observe(this, Observer {
            if (it!!.isEmpty()) {
                recyclerView.visibility = View.GONE
                noTeamsFollowedTextView.visibility = View.VISIBLE
            } else {
                noTeamsFollowedTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                viewModel.getTeamLogs(it)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (TeamsService.followedTeams.size == 0) {
            viewModel.loadFollowedTeams(true)
        } else {
            noTeamsFollowedTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            viewModel.getTeamLogs(TeamsService.followedTeams, true)
        }
    }
}
