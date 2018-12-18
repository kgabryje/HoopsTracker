package com.example.kamillog.hoopstracker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.ZonedDateTime

@Parcelize
data class GameItem(
    val id: String = "",
    val date: ZonedDateTime = ZonedDateTime.now(),
    val homeTeam: TeamItem = TeamItem(),
    val awayTeam: TeamItem = TeamItem(),
    val homeTeamScore: String = "",
    val awayTeamScore: String = ""
) : Parcelable