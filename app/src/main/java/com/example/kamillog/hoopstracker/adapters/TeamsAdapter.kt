package com.example.kamillog.hoopstracker.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kamillog.hoopstracker.R
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.viewholders.TeamsViewHolder
import kotlinx.android.synthetic.main.team_item.view.*

class TeamsAdapter(
    private val context : Context,
    private val teamsList : List<TeamItem>,
    private val onItemClick: (TeamItem) -> Any,
    private val displayChecks: Boolean = true
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.team_item, parent, false)
        return TeamsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as TeamsViewHolder
        val model: TeamItem = teamsList[position]
        viewHolder.run {
            itemView.check.visibility = View.GONE
            setTeamLogo(model.backgroundLogo) {
                if (TeamsService.followedTeams.contains(model) && displayChecks) {
                    itemView.check.visibility = View.VISIBLE
                } else {
                    itemView.check.visibility = View.GONE
                }
                itemView.setOnClickListener {
                    onItemClick(model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return teamsList.size
    }
}