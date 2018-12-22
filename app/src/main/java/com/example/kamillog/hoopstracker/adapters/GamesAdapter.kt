package com.example.kamillog.hoopstracker.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.kamillog.hoopstracker.BoxscoreActivity
import com.example.kamillog.hoopstracker.R
import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.viewholders.GameViewHolder
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class GamesAdapter(
    private val context : Context,
    var gameList : List<GameItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as GameViewHolder
        val model: GameItem = gameList[position]
        viewHolder.run {
            setHomeTeamLogo(model.homeTeam.logo)
            setHomeTeamCity(model.homeTeam.city)
            setHomeTeamName(model.homeTeam.name)
            setAwayTeamLogo(model.awayTeam.logo)
            setAwayTeamCity(model.awayTeam.city)
            setAwayTeamName(model.awayTeam.name)
            setScore(model.homeTeamScore, model.awayTeamScore)
            setGameDate(model.date.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mma", Locale.FRANCE)
                    .withZone(ZoneId.systemDefault())
            ))

            itemView.setOnClickListener {
                if (model.awayTeamScore != "" && model.homeTeamScore != "") {
                    context.startActivity(Intent(context, BoxscoreActivity::class.java).putExtra("game", model))
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return gameList.size
    }
}