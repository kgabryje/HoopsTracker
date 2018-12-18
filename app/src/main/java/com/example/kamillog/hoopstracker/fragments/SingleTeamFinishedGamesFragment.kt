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
import com.example.kamillog.hoopstracker.viewmodels.GamesViewModel

class SingleTeamFinishedGamesFragment : Fragment() {

    companion object {
        fun newInstance() = SingleTeamFinishedGamesFragment()
    }

    private lateinit var gamesAdapter: GamesAdapter
    lateinit var viewModel: GamesViewModel

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.finished_games_fragment, container, false)
        recyclerView = view.findViewById(R.id.finishedGamesRecyclerView)
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
        viewModel = ViewModelProviders.of(this).get(GamesViewModel::class.java)
        viewModel.finishedGames().observe(this, Observer {
            gamesAdapter.gameList = it!!
            gamesAdapter.notifyDataSetChanged()
        })
        val team = (activity as SingleTeamViewActivity).team
        viewModel.getTeamLogs(listOf(team), true)
    }
}
