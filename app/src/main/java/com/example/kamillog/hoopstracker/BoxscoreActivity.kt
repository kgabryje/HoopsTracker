package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.kamillog.hoopstracker.models.*
import com.example.kamillog.hoopstracker.services.LoginService
import com.example.kamillog.hoopstracker.viewmodels.BoxscoreViewModel
import com.example.kamillog.hoopstracker.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_boxscore.*
import kotlinx.android.synthetic.main.nav_header_home.view.*

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
            if (it != null) {
                boxscore_table_layout.removeAllViews()
                stats_table_layout.removeAllViews()
                createTableForQuarters(it.quartersItems)
                createBoxscore(it.homePlayerEntry, it.awayPlayerEntry)
            }
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
                LoginService.signOut()
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

    private fun createTableForQuarters(quarterItems: List<QuarterItem>) {
        val headerRow = createTableRowForSingleElement("Team", 30, 30, 30, 30)
        val homeTeamRow = createTableRowForSingleElement(
            "${gameItem.homeTeam.city} ${gameItem.homeTeam.name}",
            30, 30, 30, 30
        )
        val awayTeamRow = createTableRowForSingleElement(
            "${gameItem.awayTeam.city} ${gameItem.awayTeam.name}",
            30, 30, 30, 30
        )

        for (quarter in quarterItems) {
            val quarterNumber = if (quarter.number.toInt() < 5) {
                "Q${quarter.number}"
            } else {
                "OT${quarter.number.toInt() - 4}"
            }

            addTextViewToRow(headerRow, quarterNumber)
            addTextViewToRow(homeTeamRow, quarter.homeScore)
            addTextViewToRow(awayTeamRow, quarter.awayScore)
        }
        addTextViewToRow(headerRow, "Score")
        addTextViewToRow(homeTeamRow, gameItem.homeTeamScore)
        addTextViewToRow(awayTeamRow, gameItem.awayTeamScore)

        boxscore_table_layout.addView(headerRow)
        boxscore_table_layout.addView(homeTeamRow)
        boxscore_table_layout.addView(awayTeamRow)
    }

    private fun createBoxscore(homePlayerEntry: List<PlayerEntry>, awayPlayerEntry: List<PlayerEntry>) {
        addTableRowsForTeam(stats_table_layout, gameItem.homeTeam, homePlayerEntry)
        addTableRowsForTeam(stats_table_layout, gameItem.awayTeam, awayPlayerEntry, true)
    }

    private fun addTableRowsForTeam(table: TableLayout,
                                    team: TeamItem,
                                    playerEntries: List<PlayerEntry>,
                                    bigTopRowPadding: Boolean = false) {
        val headerTexts = listOf("Player", "Min", "Pts", "Ast", "Reb", "Stl", "Blk", "FGM", "FGA", "FG%", "3PM", "3PA", "3P%", "FTM", "FTA", "FT%", "+/-")

        var topPadding = 30
        if (bigTopRowPadding) topPadding = 120
        val teamNameRow = createTableRowForSingleElement(
            team.city + " " + team.name,
            30, topPadding, 30, 15
        )
        table.addView(teamNameRow)

        val headerRow = createTableRowWithContent(headerTexts, 30, 15, 30, 30)
        table.addView(headerRow)

        for (entry in playerEntries) {
            val row = createStatsRowFromEntry(entry)
            table.addView(row)
        }
    }

    private fun createTableRowForSingleElement(element: String,
                                               leftPadding: Int = 30,
                                               topPadding: Int = 0,
                                               rightPadding: Int = 30,
                                               bottomPadding: Int = 0): TableRow {
        return createTableRowWithContent(listOf(element), leftPadding, topPadding, rightPadding, bottomPadding)
    }

    private fun createTableRowWithContent(content: List<String>,
                                          leftPadding: Int = 30,
                                          topPadding: Int = 0,
                                          rightPadding: Int = 30,
                                          bottomPadding: Int = 0): TableRow {
        val row = TableRow(this)
        row.setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
        row.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        for (element in content) {
            addTextViewToRow(row, element)
        }

        return row
    }

    private fun createStatsRowFromEntry(entry: PlayerEntry): TableRow {
        val row = TableRow(this)
        row.setPadding(30, 15, 30, 15)
        row.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        val seconds: Int = entry.stats.seconds.value.toInt()
        val minutesString = (seconds/60).toString()
        var secondsToString = (seconds%60).toString()
        if (secondsToString.length == 1) {
            secondsToString = "0$secondsToString"
        }

        addTextViewToRow(row, entry.player.firstName + " " + entry.player.lastName)
        addTextViewToRow(row, "$minutesString:$secondsToString")
        addTextViewToRow(row, entry.stats.points.value)
        addTextViewToRow(row, entry.stats.assists.value)
        addTextViewToRow(row, entry.stats.rebounds.value)
        addTextViewToRow(row, entry.stats.steals.value)
        addTextViewToRow(row, entry.stats.blocks.value)
        addTextViewToRow(row, entry.stats.fieldGoalsMade.value)
        addTextViewToRow(row, entry.stats.fieldGoalsAttempts.value)
        addTextViewToRow(row, entry.stats.fieldGoalsPercent.value)
        addTextViewToRow(row, entry.stats.threePointMade.value)
        addTextViewToRow(row, entry.stats.threePointAttempts.value)
        addTextViewToRow(row, entry.stats.threePointPercent.value)
        addTextViewToRow(row, entry.stats.freeThrowMade.value)
        addTextViewToRow(row, entry.stats.freeThrowAttempts.value)
        addTextViewToRow(row, entry.stats.freeThrowPercent.value)
        addTextViewToRow(row, entry.stats.plusMinus.value)

        return row
    }

    private fun addTextViewToRow(row: TableRow,
                                 text: String,
                                 leftPadding: Int = 30,
                                 topPadding: Int = 0,
                                 rightPadding: Int = 30,
                                 bottomPadding: Int = 0) {
        val textView = TextView(this)
        textView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding)
        textView.text = text
        row.addView(textView)
    }
}
