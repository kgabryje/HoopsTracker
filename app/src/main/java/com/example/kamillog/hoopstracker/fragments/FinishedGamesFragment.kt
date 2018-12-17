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

import com.example.kamillog.hoopstracker.R
import com.example.kamillog.hoopstracker.adapters.GamesAdapter
import com.example.kamillog.hoopstracker.services.GamesService
import com.example.kamillog.hoopstracker.services.TeamsService

class FinishedGamesFragment : Fragment() {

    companion object {
        fun newInstance() = FinishedGamesFragment()
    }

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var viewModel: FinishedGamesViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.finished_games_fragment, container, false)
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
        viewModel = ViewModelProviders.of(this).get(FinishedGamesViewModel::class.java)
        viewModel.finishedGames().observe(this, Observer {
            gamesAdapter.gameList = it!!
            gamesAdapter.notifyDataSetChanged()
        })
        viewModel.followedTeams().observe(this, Observer {
            viewModel.getTeamLogs(it!!)
        })
    }

    override fun onStart() {
        super.onStart()
        if (TeamsService.followedTeams.size == 0) {
            viewModel.loadFollowedTeams()
        } else {
            viewModel.getTeamLogs(TeamsService.followedTeams, true)
        }
    }

}
