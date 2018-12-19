package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import com.example.kamillog.hoopstracker.models.*
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.viewmodels.BoxscoreViewModel
import com.example.kamillog.hoopstracker.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_boxscore.*
import kotlinx.android.synthetic.main.nav_header_home.view.*
import org.w3c.dom.Text

class BoxscoreActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var viewModel: BoxscoreViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var gameItem: GameItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boxscore)
        setSupportActionBar(toolbarBoxscore)

        gameItem = intent.getParcelableExtra("game")

        supportActionBar!!.title = "${gameItem.homeTeam.name} vs ${gameItem.awayTeam.name}"

        viewModel = ViewModelProviders.of(this).get(BoxscoreViewModel::class.java)

        viewModel.boxscore().observe(this, Observer {
            boxscore_table_layout.removeAllViews()
            createTableForQuarters(it!!.quartersItems)
            createBoxscore(it.homePlayerEntry, it.awayPlayerEntry)
        })
        viewModel.getBoxscore(gameItem)

        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.userModel().value = UserModel()
        userViewModel.userModel().observe(this, Observer {
            navViewBoxscore.setNavigationItemSelectedListener(this)
            navViewBoxscore.getHeaderView(0).navbarEmailID.text = it?.email
        })
        userViewModel.setUserData()

        val toggle = ActionBarDrawerToggle(
            this, drawerLayoutBoxscore, toolbarBoxscore, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayoutBoxscore.addDrawerListener(toggle)
        toggle.syncState()
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
        drawerLayoutBoxscore.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayoutBoxscore.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutBoxscore.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
            finish()
        }
    }

    fun createTableForQuarters(quarterItems: List<QuarterItem>) {
        val tableRowHome = TableRow(this)
        val tableRowAway = TableRow(this)
        val headerRow = TableRow(this)
        tableRowHome.setPadding(30, 30, 30, 30)
        tableRowHome.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        tableRowAway.setPadding(30, 30, 30, 30)
        tableRowAway.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        headerRow.setPadding(30, 30, 30, 30)
        headerRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        val homeTeamNameText = TextView(this)
        val awayTeamNameText = TextView(this)
        val headerTeamText = TextView(this)
        homeTeamNameText.setPadding(30, 0, 30, 0)
        homeTeamNameText.text = "${gameItem.homeTeam.city} ${gameItem.homeTeam.name}"
        awayTeamNameText.setPadding(30, 0, 30, 0)
        awayTeamNameText.text = "${gameItem.awayTeam.city} ${gameItem.awayTeam.name}"
        headerTeamText.setPadding(30, 0, 30, 0)
        headerTeamText.text = "Team"

        tableRowHome.addView(homeTeamNameText)
        tableRowAway.addView(awayTeamNameText)
        headerRow.addView(headerTeamText)

        for (quarter in quarterItems) {
            val quarterHomeScore = TextView(this)
            quarterHomeScore.text = quarter.homeScore
            quarterHomeScore.setPadding(30, 0, 30, 0)

            val quarterAwayScore = TextView(this)
            quarterAwayScore.text = quarter.awayScore
            quarterAwayScore.setPadding(30, 0, 30, 0)

            val quarterNumber = TextView(this)
            quarterNumber.text = "Q${quarter.number}"
            quarterNumber.setPadding(30, 0, 30, 0)

            tableRowHome.addView(quarterHomeScore)
            tableRowAway.addView(quarterAwayScore)
            headerRow.addView(quarterNumber)
        }

        val homeTeamScore = TextView(this)
        val awayTeamScore = TextView(this)
        val headerScore = TextView(this)
        homeTeamScore.setPadding(30, 0, 30, 0)
        homeTeamScore.text = "${gameItem.homeTeamScore}"
        awayTeamScore.setPadding(30, 0, 30, 0)
        awayTeamScore.text = "${gameItem.awayTeamScore}"
        headerScore.setPadding(30, 0, 30, 0)
        headerScore.text = "TOT"

        tableRowHome.addView(homeTeamScore)
        tableRowAway.addView(awayTeamScore)
        headerRow.addView(headerScore)

        boxscore_table_layout.addView(headerRow)
        boxscore_table_layout.addView(tableRowHome)
        boxscore_table_layout.addView(tableRowAway)
    }

    fun createBoxscore(homePlayerEntry: List<PlayerEntry>, awayPlayerEntry: List<PlayerEntry>) {
        val homeTeamNameRow = TableRow(this)
        homeTeamNameRow.setPadding(30, 30, 30, 15)
        homeTeamNameRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        val homeTeamNameText = TextView(this)
        homeTeamNameText.setPadding(30, 0, 30, 0)
        homeTeamNameText.text = gameItem.homeTeam.city + " " + gameItem.homeTeam.name
        homeTeamNameRow.addView(homeTeamNameText)

        stats_table_layout.addView(homeTeamNameRow)

        val headerTexts = listOf("Player", "Min", "Pts", "Ast", "Reb", "Stl", "Blk", "FGM", "FGA", "FG%", "3PM", "3PA", "3P%", "FTM", "FTA", "FT%", "+/-")

        val headerRow = TableRow(this)
        headerRow.setPadding(30, 15, 30, 30)
        headerRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        for (columnName in headerTexts) {
            val headerText = TextView(this)
            headerText.setPadding(30, 0, 30, 0)
            headerText.text = columnName

            headerRow.addView(headerText)
        }

        stats_table_layout.addView(headerRow)


        for (entry in homePlayerEntry) {
            val row = TableRow(this)
            row.setPadding(30, 15, 30, 15)
            row.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

            val playerNameTextView = TextView(this)
            playerNameTextView.setPadding(30, 0, 30, 0)
            playerNameTextView.text = entry.player.firstName + " " + entry.player.lastName
            row.addView(playerNameTextView)

            val minutesTextView = TextView(this)
            minutesTextView.setPadding(30, 0, 30, 0)
            val seconds: Int = entry.stats.seconds.value.toInt()
            val minutesString = (seconds/60).toString()
            var secondsToString = (seconds%60).toString()
            if (secondsToString.length == 1) {
                secondsToString = "0" + secondsToString
            }
            minutesTextView.text = "$minutesString:$secondsToString"
            row.addView(minutesTextView)

            val pointsTextView = TextView(this)
            pointsTextView.setPadding(30, 0, 30, 0)
            pointsTextView.text = entry.stats.points.value
            row.addView(pointsTextView)

            val assistsTextView = TextView(this)
            assistsTextView.setPadding(30, 0, 30, 0)
            assistsTextView.text = entry.stats.assists.value
            row.addView(assistsTextView)

            val reboundsTextView = TextView(this)
            reboundsTextView.setPadding(30, 0, 30, 0)
            reboundsTextView.text = entry.stats.rebounds.value
            row.addView(reboundsTextView)

            val stealsTextView = TextView(this)
            stealsTextView.setPadding(30, 0, 30, 0)
            stealsTextView.text = entry.stats.steals.value
            row.addView(stealsTextView)

            val blocksTextView = TextView(this)
            blocksTextView.setPadding(30, 0, 30, 0)
            blocksTextView.text = entry.stats.blocks.value
            row.addView(blocksTextView)

            val fgmTextView = TextView(this)
            fgmTextView.setPadding(30, 0, 30, 0)
            fgmTextView.text = entry.stats.fieldGoalsMade.value
            row.addView(fgmTextView)

            val fgaTextView = TextView(this)
            fgaTextView.setPadding(30, 0, 30, 0)
            fgaTextView.text = entry.stats.fieldGoalsAttempts.value
            row.addView(fgaTextView)

            val fgpTextView = TextView(this)
            fgpTextView.setPadding(30, 0, 30, 0)
            fgpTextView.text = entry.stats.fieldGoalsPercent.value
            row.addView(fgpTextView)

            val tpmTextView = TextView(this)
            tpmTextView.setPadding(30, 0, 30, 0)
            tpmTextView.text = entry.stats.threePointMade.value
            row.addView(tpmTextView)

            val tpaTextView = TextView(this)
            tpaTextView.setPadding(30, 0, 30, 0)
            tpaTextView.text = entry.stats.threePointAttempts.value
            row.addView(tpaTextView)

            val tppTextView = TextView(this)
            tppTextView.setPadding(30, 0, 30, 0)
            tppTextView.text = entry.stats.threePointPercent.value
            row.addView(tppTextView)

            val ftmTextView = TextView(this)
            ftmTextView.setPadding(30, 0, 30, 0)
            ftmTextView.text = entry.stats.freeThrowMade.value
            row.addView(ftmTextView)

            val ftaTextView = TextView(this)
            ftaTextView.setPadding(30, 0, 30, 0)
            ftaTextView.text = entry.stats.freeThrowAttempt.value
            row.addView(ftaTextView)

            val ftpTextView = TextView(this)
            ftpTextView.setPadding(30, 0, 30, 0)
            ftpTextView.text = entry.stats.freeThrowPercent.value
            row.addView(ftpTextView)

            val plusMinusTextView = TextView(this)
            plusMinusTextView.setPadding(30, 0, 30, 0)
            plusMinusTextView.text = entry.stats.plusMinus.value
            row.addView(plusMinusTextView)

            stats_table_layout.addView(row)
        }


        val awayTeamNameRow = TableRow(this)
        awayTeamNameRow.setPadding(30, 120, 30, 15)
        awayTeamNameRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        val awayTeamNameText = TextView(this)
        awayTeamNameText.setPadding(30, 0, 30, 0)
        awayTeamNameText.text = gameItem.awayTeam.city + " " + gameItem.awayTeam.name
        awayTeamNameRow.addView(awayTeamNameText)

        stats_table_layout.addView(awayTeamNameRow)

        val headerRow2 = TableRow(this)
        headerRow2.setPadding(30, 15, 30, 30)
        headerRow2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        for (columnName in headerTexts) {
            val headerText = TextView(this)
            headerText.setPadding(30, 0, 30, 0)
            headerText.text = columnName

            headerRow2.addView(headerText)
        }

        stats_table_layout.addView(headerRow2)

        for (entry in awayPlayerEntry) {
            val row = TableRow(this)
            row.setPadding(30, 15, 30, 15)
            row.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

            val playerNameTextView = TextView(this)
            playerNameTextView.setPadding(30, 0, 30, 0)
            playerNameTextView.text = entry.player.firstName + " " + entry.player.lastName
            row.addView(playerNameTextView)

            val minutesTextView = TextView(this)
            minutesTextView.setPadding(30, 0, 30, 0)
            val seconds: Int = entry.stats.seconds.value.toInt()
            val minutesString = (seconds/60).toString()
            var secondsToString = (seconds%60).toString()
            if (secondsToString.length == 1) {
                secondsToString = "0" + secondsToString
            }
            minutesTextView.text = "$minutesString:$secondsToString"
            row.addView(minutesTextView)

            val pointsTextView = TextView(this)
            pointsTextView.setPadding(30, 0, 30, 0)
            pointsTextView.text = entry.stats.points.value
            row.addView(pointsTextView)

            val assistsTextView = TextView(this)
            assistsTextView.setPadding(30, 0, 30, 0)
            assistsTextView.text = entry.stats.assists.value
            row.addView(assistsTextView)

            val reboundsTextView = TextView(this)
            reboundsTextView.setPadding(30, 0, 30, 0)
            reboundsTextView.text = entry.stats.rebounds.value
            row.addView(reboundsTextView)

            val stealsTextView = TextView(this)
            stealsTextView.setPadding(30, 0, 30, 0)
            stealsTextView.text = entry.stats.steals.value
            row.addView(stealsTextView)

            val blocksTextView = TextView(this)
            blocksTextView.setPadding(30, 0, 30, 0)
            blocksTextView.text = entry.stats.blocks.value
            row.addView(blocksTextView)

            val fgmTextView = TextView(this)
            fgmTextView.setPadding(30, 0, 30, 0)
            fgmTextView.text = entry.stats.fieldGoalsMade.value
            row.addView(fgmTextView)

            val fgaTextView = TextView(this)
            fgaTextView.setPadding(30, 0, 30, 0)
            fgaTextView.text = entry.stats.fieldGoalsAttempts.value
            row.addView(fgaTextView)

            val fgpTextView = TextView(this)
            fgpTextView.setPadding(30, 0, 30, 0)
            fgpTextView.text = entry.stats.fieldGoalsPercent.value
            row.addView(fgpTextView)

            val tpmTextView = TextView(this)
            tpmTextView.setPadding(30, 0, 30, 0)
            tpmTextView.text = entry.stats.threePointMade.value
            row.addView(tpmTextView)

            val tpaTextView = TextView(this)
            tpaTextView.setPadding(30, 0, 30, 0)
            tpaTextView.text = entry.stats.threePointAttempts.value
            row.addView(tpaTextView)

            val tppTextView = TextView(this)
            tppTextView.setPadding(30, 0, 30, 0)
            tppTextView.text = entry.stats.threePointPercent.value
            row.addView(tppTextView)

            val ftmTextView = TextView(this)
            ftmTextView.setPadding(30, 0, 30, 0)
            ftmTextView.text = entry.stats.freeThrowMade.value
            row.addView(ftmTextView)

            val ftaTextView = TextView(this)
            ftaTextView.setPadding(30, 0, 30, 0)
            ftaTextView.text = entry.stats.freeThrowAttempt.value
            row.addView(ftaTextView)

            val ftpTextView = TextView(this)
            ftpTextView.setPadding(30, 0, 30, 0)
            ftpTextView.text = entry.stats.freeThrowPercent.value
            row.addView(ftpTextView)

            val plusMinusTextView = TextView(this)
            plusMinusTextView.setPadding(30, 0, 30, 0)
            plusMinusTextView.text = entry.stats.plusMinus.value
            row.addView(plusMinusTextView)

            stats_table_layout.addView(row)
        }
    }
}
