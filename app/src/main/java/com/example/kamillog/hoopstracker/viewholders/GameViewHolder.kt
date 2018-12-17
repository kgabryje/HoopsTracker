package com.example.kamillog.hoopstracker.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.game_item.view.*

class GameViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun setHomeTeamLogo(context: Context, homeTeamLogo: String) {
        val homeTeamLogoView: ImageView = view.home_team_logo
        homeTeamLogo.let {
            Picasso.with(context).load(it).into(homeTeamLogoView)
        }
    }

    fun setHomeTeamCity(city: String) {
        val cityTextView: TextView = view.home_team_city
        cityTextView.text = city
    }

    fun setHomeTeamName(city: String) {
        val nameTextView: TextView = view.home_team_name
        nameTextView.text = city
    }

    fun setAwayTeamLogo(context: Context, awayTeamLogo: String) {
        val awayTeamLogoView: ImageView = view.away_team_logo
        awayTeamLogo.let {
            Picasso.with(context).load(it).into(awayTeamLogoView)
        }
    }

    fun setAwayTeamCity(city: String) {
        val cityTextView: TextView = view.away_team_city
        cityTextView.text = city
    }

    fun setAwayTeamName(city: String) {
        val nameTextView: TextView = view.away_team_name
        nameTextView.text = city
    }

    fun setScore(homeTeamScore: String, awayTeamScore: String) {
        val scoreView: TextView = view.score
        if(homeTeamScore != "" && awayTeamScore != "") {
            scoreView.text = "$homeTeamScore - $awayTeamScore"
        } else {
            scoreView.text = ""
        }
    }

    fun setGameDate(date: String) {
        val dateView: TextView = view.game_date
        dateView.text = date
    }
}