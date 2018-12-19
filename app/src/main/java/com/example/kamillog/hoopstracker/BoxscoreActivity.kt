package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.viewmodels.BoxscoreViewModel

class BoxscoreActivity : AppCompatActivity() {
    private lateinit var viewModel: BoxscoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boxscore)

        val gameItem = intent.getParcelableExtra<GameItem>("game")
        viewModel = ViewModelProviders.of(this).get(BoxscoreViewModel::class.java)

        viewModel.boxscore().observe(this, Observer {
        })
        viewModel.getBoxscore(gameItem)
    }
}
