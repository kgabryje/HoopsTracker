package com.example.kamillog.hoopstracker.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.team_item.view.*
import java.lang.Exception

class TeamsViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun setTeamLogo(logoUrl: String, onPictureLoaded: ()->Unit) {
        if (logoUrl == "") return
        val teamLogo: ImageView = view.team_logo
        Picasso.get().load(logoUrl).into(teamLogo, object: Callback {
            override fun onSuccess() {
                onPictureLoaded()
            }

            override fun onError(e: Exception?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}