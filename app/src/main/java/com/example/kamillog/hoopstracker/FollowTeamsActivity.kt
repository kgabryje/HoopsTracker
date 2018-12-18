package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.kamillog.hoopstracker.adapters.TeamsAdapter
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.models.UserModel
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.viewmodels.FollowTeamsViewModel
import com.example.kamillog.hoopstracker.viewmodels.UserViewModel
import com.example.kamillog.hoopstracker.viewmodels.ViewModelFactory
import kotlinx.android.synthetic.main.activity_follow_teams.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class FollowTeamsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var viewModel: FollowTeamsViewModel
    private lateinit var userViewModel: UserViewModel

    private val teamsAdapter = TeamsAdapter(this, TeamsService.teams, ::toggleTeam)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_teams)
        setSupportActionBar(toolbarFollowTeams)

        followTeamsRecyclerView.apply {
            adapter = teamsAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        }

        viewModel = ViewModelProviders.of(this, ViewModelFactory(this, "FollowTeams"))
            .get(FollowTeamsViewModel::class.java)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        viewModel.followedTeams().observe(this, Observer {
            teamsAdapter.notifyDataSetChanged()
        })

        viewModel.loadFollowedTeams()

        userViewModel.userModel().value = UserModel()
        userViewModel.userModel().observe(this, Observer {
            navViewFollowTeams.setNavigationItemSelectedListener(this)
            navViewFollowTeams.getHeaderView(0).navbarEmailID.text = it?.email
        })
        userViewModel.setUserData()

        val toggle = ActionBarDrawerToggle(
            this, drawerLayoutFollowTeams, toolbarFollowTeams, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayoutFollowTeams.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun toggleTeam(teamItem: TeamItem) {
        viewModel.toggleTeam(teamItem)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            R.id.nav_follow_teams -> {}
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
        drawerLayoutFollowTeams.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayoutFollowTeams.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutFollowTeams.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            finish()
        }
    }
}
