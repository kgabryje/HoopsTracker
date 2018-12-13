package com.example.kamillog.hoopstracker.viewholders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.team_item.view.*

class TeamsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun setTeamLogo(context: Context, logoUrl: String) {
        val teamLogo: ImageView = view.team_logo
        Picasso.with(context).load(logoUrl).into(teamLogo)
    }
}