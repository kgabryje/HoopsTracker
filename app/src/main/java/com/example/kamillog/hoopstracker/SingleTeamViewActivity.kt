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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.example.kamillog.hoopstracker.adapters.SingleTeamFragmentAdapter
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.models.UserModel
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.utils.ToastMessageHandler
import com.example.kamillog.hoopstracker.viewmodels.GamesViewModel
import com.example.kamillog.hoopstracker.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_single_team_view.*
import kotlinx.android.synthetic.main.app_bar_single_team.*
import kotlinx.android.synthetic.main.content_single_team.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import android.support.v4.content.IntentCompat
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK



class SingleTeamViewActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var team: TeamItem
    private lateinit var viewModel: UserViewModel
    private lateinit var gamesViewModel: GamesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_team_view)
        setSupportActionBar(toolbarSingleTeam)
        team = intent.getParcelableExtra("team") as TeamItem
        supportActionBar?.title = "${team.city} ${team.name}"
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        viewModel.userModel().value = UserModel()
        viewModel.userModel().observe(this, Observer {
            navViewSingleTeam.setNavigationItemSelectedListener(this)
            navViewSingleTeam.getHeaderView(0).navbarEmailID.text = it?.email
        })
        viewModel.setUserData()

        gamesViewModel = ViewModelProviders.of(this).get(GamesViewModel::class.java)

        singleTeamViewPager.adapter = SingleTeamFragmentAdapter(supportFragmentManager)
        singleTeamTab.setupWithViewPager(singleTeamViewPager)
        val root: View = singleTeamTab.getChildAt(0)
        if (root is LinearLayout) {
            val drawable = GradientDrawable().apply {
                setColor(ContextCompat.getColor(this@SingleTeamViewActivity, R.color.colorWhite))
                setSize(3, 2)
            }
            root.run {
                showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
                dividerPadding = 10
                dividerDrawable = drawable
            }
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayoutSingleTeam, toolbarSingleTeam, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayoutSingleTeam.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawerLayoutSingleTeam.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutSingleTeam.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            R.id.nav_follow_teams -> {
                startActivity(Intent(this, FollowTeamsActivity::class.java))
                finish()
            }
            R.id.nav_my_teams -> {
                startActivity(Intent(this, MyTeamsActivity::class.java))
                finish()
            }
            R.id.nav_logout-> {
                LoginService().signOut()
                startActivity(Intent(this, LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
                finish()
            }
        }
        drawerLayoutSingleTeam.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.refresh_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.refresh_btn -> {
            gamesViewModel.getUpcomingGames(listOf(team), true)
            gamesViewModel.getTeamLogs(listOf(team), true)
            ToastMessageHandler(this).showToastMessage("Games refreshed")
            true
        }
        else -> false
    }
}
