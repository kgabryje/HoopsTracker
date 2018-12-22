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
import com.example.kamillog.hoopstracker.SingleTeamViewActivity
import com.example.kamillog.hoopstracker.adapters.GamesAdapter
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.viewmodels.GamesViewModel

class SingleTeamUpcomingGamesFragment : Fragment() {

    companion object {
        fun newInstance() = SingleTeamUpcomingGamesFragment()
    }

    private lateinit var gamesAdapter: GamesAdapter
    private lateinit var viewModel: GamesViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.upcoming_games_fragment, container, false)
        recyclerView = view.findViewById(R.id.upcomingGamesRecyclerView)
        gamesAdapter = GamesAdapter(activity as Activity, listOf())
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
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(GamesViewModel::class.java)
        }
        viewModel.upcomingGames().observe(this, Observer {
            if (it != null) {
                gamesAdapter.gameList = it
                gamesAdapter.notifyDataSetChanged()
            }
        })
        val team = (activity as SingleTeamViewActivity).team
        viewModel.getUpcomingGames(listOf(team), TeamsService.followedTeamsChanged, true)
    }

}
