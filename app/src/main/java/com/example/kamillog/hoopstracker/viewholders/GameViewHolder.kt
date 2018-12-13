package com.example.kamillog.hoopstracker.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.kamillog.hoopstracker.models.TeamItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_item.view.*
import java.util.*

class GameViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun setHomeTeam(context: Context, homeTeam: String?) {
        val homeTeamLogo: ImageView = view.home_team_logo
        homeTeam?.let {
            Picasso.with(context).load(it).into(homeTeamLogo)
        }
    }

//    fun setAwayTeam(context: Context, awayTeam: String?) {
//        val awayTeamLogo: ImageView = view.away_team_logo
//        awayTeam?.let {
//            Picasso.with(context).load(it).into(awayTeamLogo)
//        }
//    }
//
//    fun setScore(homeTeamScore: Int?, awayTeamScore: Int?) {
//        val scoreView: TextView = view.score
//        if(homeTeamScore != null && awayTeamScore != null) {
//            scoreView.text = "$homeTeamScore - $awayTeamScore"
//        } else {
//            scoreView.text = "Upcoming"
//        }
//    }

    fun setGameDate(date: String) {
        val dateView: TextView = view.game_date
        dateView.text = date
    }
}