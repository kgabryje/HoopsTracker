package com.example.kamillog.hoopstracker

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.widget.TableRow
import android.widget.TextView
import com.example.kamillog.hoopstracker.models.GameItem
import com.example.kamillog.hoopstracker.models.PlayerEntry
import com.example.kamillog.hoopstracker.models.QuarterItem
import com.example.kamillog.hoopstracker.models.TeamItem
import com.example.kamillog.hoopstracker.viewmodels.BoxscoreViewModel
import kotlinx.android.synthetic.main.activity_boxscore.*
import org.w3c.dom.Text

class BoxscoreActivity : AppCompatActivity() {
    private lateinit var viewModel: BoxscoreViewModel
    private lateinit var gameItem: GameItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_boxscore)

        gameItem = intent.getParcelableExtra<GameItem>("game")
        viewModel = ViewModelProviders.of(this).get(BoxscoreViewModel::class.java)

        viewModel.boxscore().observe(this, Observer {
            boxscore_table_layout.removeAllViews()
            createTableForQuarters(it!!.quartersItems)
            createBoxscore(it.homePlayerEntry, it.awayPlayerEntry)
        })
        viewModel.getBoxscore(gameItem)
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
        table_home_team_name.text = gameItem.homeTeam.city + " " + gameItem.homeTeam.name
        table_home_team_name.setPadding(30, 0, 30, 0)

        table_away_team_name.text = gameItem.awayTeam.city + " " + gameItem.awayTeam.name
        table_away_team_name.setPadding(30, 0, 30, 0)

        val headerTexts = listOf("Player", "Min", "Pts", "Ast", "Reb")

        val headerRow = TableRow(this)
        headerRow.setPadding(30, 30, 30, 30)
        headerRow.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        for (columnName in headerTexts) {
            val headerText = TextView(this)
            headerText.setPadding(30, 0, 30, 0)
            headerText.text = columnName

            headerRow.addView(headerText)
        }

        home_stats_table_layout.addView(headerRow)

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

            home_stats_table_layout.addView(row)
        }


        val headerRow2 = TableRow(this)
        headerRow2.setPadding(30, 30, 30, 30)
        headerRow2.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)

        for (columnName in headerTexts) {
            val headerText = TextView(this)
            headerText.setPadding(30, 0, 30, 0)
            headerText.text = columnName

            headerRow2.addView(headerText)
        }

        away_stats_table_layout.addView(headerRow2)

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
            minutesTextView.text = "${seconds/60}:${seconds%60}"
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

            away_stats_table_layout.addView(row)
        }
    }
}
