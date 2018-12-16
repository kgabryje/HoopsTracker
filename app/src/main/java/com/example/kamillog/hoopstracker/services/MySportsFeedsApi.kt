package com.example.kamillog.hoopstracker.services

import com.example.kamillog.hoopstracker.models.ScheduleEndpoint
import com.example.kamillog.hoopstracker.models.TeamGameLogsEndpoint
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MySportsFeedsApi {
    @GET("{season_name}/team_gamelogs.json")
    fun getTeamGameLogs(
        @Path("season_name") seasonName: String,
        @Query("team") teamList: String,
        @Query("date") dateRange: String
    ): Call<TeamGameLogsEndpoint>

    @GET("{season_name}/full_game_schedule.json")
    fun getSchedule(
        @Path("season_name") seasonName: String,
        @Query("team") teamList: String,
        @Query("date") dateRange: String
    ): Call<ScheduleEndpoint>
}