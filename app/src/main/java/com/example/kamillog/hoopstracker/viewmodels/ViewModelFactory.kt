package com.example.kamillog.hoopstracker.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context

class ViewModelFactory(val context: Context, val type: String) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (type) {
            "Login" -> LoginViewModel(context) as T
            "Register" -> RegisterViewModel(context) as T
            "FollowTeams" -> FollowTeamsViewModel(context) as T
            "Home" -> HomeViewModel() as T
            else -> throw IllegalArgumentException("Pass correct factory type")
        }
    }
}