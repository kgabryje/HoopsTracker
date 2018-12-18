package com.example.kamillog.hoopstracker.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeamItem(
    val city: String = "",
    val name: String = "",
    val logo: String = "",
    val backgroundLogo: String = ""
) : Parcelable