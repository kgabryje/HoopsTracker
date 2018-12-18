package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.example.kamillog.hoopstracker.adapters.HomeFragmentAdapter
import com.example.kamillog.hoopstracker.models.UserModel
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import com.example.kamillog.hoopstracker.R.id.refresh_btn
import com.example.kamillog.hoopstracker.viewmodels.GamesViewModel
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.utils.ToastMessageHandler

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var viewModel: UserViewModel
    private lateinit var gamesViewModel: GamesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        Log.d("home", "oncreate")
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        viewModel.userModel().value = UserModel()
        viewModel.userModel().observe(this, Observer {
            navView.setNavigationItemSelectedListener(this)
            navView.getHeaderView(0).navbarEmailID.text = it?.email
        })
        viewModel.setUserData()

        gamesViewModel = ViewModelProviders.of(this).get(GamesViewModel::class.java)
        //Add adapter to pageView
        homeViewPager.adapter = HomeFragmentAdapter(supportFragmentManager)
        homeTab.setupWithViewPager(homeViewPager)
        val root: View = homeTab.getChildAt(0)
        if (root is LinearLayout) {
            val drawable = GradientDrawable().apply {
                setColor(ContextCompat.getColor(this@HomeActivity, R.color.colorWhite))
                setSize(3, 2)
            }
            root.run {
                showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
                dividerPadding = 10
                dividerDrawable = drawable
            }
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.refresh_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        refresh_btn -> {
            gamesViewModel.getUpcomingGames(TeamsService.followedTeams, true)
            gamesViewModel.getTeamLogs(TeamsService.followedTeams, true)
            ToastMessageHandler(this).showToastMessage("Games refreshed")
            true
        }
        else -> false
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
            }
            R.id.nav_follow_teams -> {
                startActivity(Intent(this, FollowTeamsActivity::class.java))
            }
            R.id.nav_my_teams -> {
                startActivity(Intent(this, MyTeamsActivity::class.java))
            }

            R.id.nav_logout -> {
                LoginService().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun textViewOnClick(v: View) {
        startActivity(Intent(this, FollowTeamsActivity::class.java))
    }
}
