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

class UpcomingGamesFragment : Fragment() {

    companion object {
        fun newInstance() = UpcomingGamesFragment()
    }

    private lateinit var viewModel: GamesViewModel
    private lateinit var gamesAdapter: GamesAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var noTeamsFollowedTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.upcoming_games_fragment, container, false)
        noTeamsFollowedTextView = view.findViewById(R.id.upcoming_games_no_teams_followed)
        recyclerView = view.findViewById(R.id.upcomingGamesRecyclerView)
        gamesAdapter = GamesAdapter(activity as Activity, GamesService.upcomingGames)
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

        activity?.let { parent ->
            viewModel = ViewModelProviders.of(parent).get(GamesViewModel::class.java)
        }
        viewModel.upcomingGames().observe(this, Observer {
            if (it != null) {
                gamesAdapter.gameList = it
                gamesAdapter.notifyDataSetChanged()
            }
        })
        viewModel.followedTeams().observe(this, Observer {
            it?.run {
                if (it.isEmpty()) {
                    recyclerView.visibility = View.GONE
                    noTeamsFollowedTextView.visibility = View.VISIBLE
                } else {
                    noTeamsFollowedTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    viewModel.getUpcomingGames(it)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (TeamsService.followedTeams.size > 0) {
            noTeamsFollowedTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}
