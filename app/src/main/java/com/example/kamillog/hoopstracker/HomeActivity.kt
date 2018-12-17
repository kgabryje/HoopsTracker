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
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import com.example.kamillog.hoopstracker.adapters.MyFragmentAdapter
import com.example.kamillog.hoopstracker.models.UserModel
import com.example.kamillog.hoopstracker.viewmodels.HomeViewModel
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        setSupportActionBar(toolbar)
        Log.d("home", "oncreate")
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        viewModel.userModel().value = UserModel()
        viewModel.userModel().observe(this, Observer {
            navView.setNavigationItemSelectedListener(this)
            navView.getHeaderView(0).navbarEmailID.text = it?.email
        })
        viewModel.setUserData()

        //Add adapter to pageView
        homeViewPager.adapter = MyFragmentAdapter(supportFragmentManager)
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

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.home, menu)
//        return true
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_follow_teams -> {
                startActivity(Intent(this, FollowTeamsActivity::class.java))
            }
            R.id.nav_my_teams -> {
                startActivity(Intent(this, MyTeamsActivity::class.java))
            }

            R.id.nav_logout-> {
                signOut()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun signOut(): Boolean {
        viewModel.signOut()
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        finish()
        return true
    }

    override fun onDestroy() {
        Log.d("home", "ondestroy")
        super.onDestroy()
    }

    override fun onResume() {
        Log.d("home", "onResume")
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        Log.d("home", "onstart")
    }

    override fun onRestart() {
        Log.d("home", "onrestart")
        super.onRestart()
    }

    override fun onPause() {
        Log.d("home", "onpause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("home", "onstop")
        super.onStop()
    }
}
