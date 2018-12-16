package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kamillog.hoopstracker.viewmodels.HomeViewModel
import com.example.kamillog.hoopstracker.viewmodels.ViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this, "Home"))
            .get(HomeViewModel::class.java)
        viewModel.finishedGames().observe(this, Observer {  })
        viewModel.upcomingGames().observe(this, Observer {  })
        viewModel.followedTeams().observe(this, Observer {
            viewModel.getTeamLogs(it!!)
            viewModel.getUpcomingGames(it)
        })
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel.loadFollowedTeams()
    }
}
