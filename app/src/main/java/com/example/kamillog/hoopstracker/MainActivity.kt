package com.example.kamillog.hoopstracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        val mainIntent = Intent(this, FollowTeamsActivity::class.java)
        startActivity(mainIntent)
        finish()
    }
}
