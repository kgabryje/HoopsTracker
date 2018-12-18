package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.example.kamillog.hoopstracker.adapters.TeamsAdapter
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.models.UserModel
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.services.TeamsService
import com.example.kamillog.hoopstracker.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_follow_teams.*
import kotlinx.android.synthetic.main.activity_my_teams.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class MyTeamsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val mAdapter = TeamsAdapter(this, TeamsService.followedTeams, ::onClick, false)
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_teams)
        setSupportActionBar(toolbarMyTeams)

        myTeamsRecyclerView.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
        }

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        userViewModel.userModel().value = UserModel()
        userViewModel.userModel().observe(this, Observer {
            navViewMyTeams.setNavigationItemSelectedListener(this)
            navViewMyTeams.getHeaderView(0).navbarEmailID.text = it?.email
        })
        userViewModel.setUserData()

        val toggle = ActionBarDrawerToggle(
            this, drawerLayoutMyTeams, toolbarFollowTeams, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayoutMyTeams.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onStart() {
        super.onStart()
        mAdapter.notifyDataSetChanged()
    }

    fun onClick(team: TeamItem) {
        val intent = Intent(this, SingleTeamViewActivity::class.java).putExtra("team", team)
        startActivity(intent)
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
            R.id.nav_my_teams -> {}

            R.id.nav_logout-> {
                LoginService().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        drawerLayoutMyTeams.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayoutMyTeams.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutMyTeams.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            finish()
        }
    }
}
