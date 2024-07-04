package com.soccersim.data.datasources.teams

import com.soccersim.data.models.TeamEntity
import kotlinx.coroutines.delay

/*
* Ideally local data source should handle DB interactions
* I used list to simplify logic, as using a database was not a requirement for this assignment
* */
class LocalTeamsDataSource : TeamsDataSource {

    private val teams = listOf(
        TeamEntity(1, "Team A", 5),
        TeamEntity(2, "Team B", 4),
        TeamEntity(3, "Team C", 3),
        TeamEntity(4, "Team D", 2)
    )

    override suspend fun getTeams(): Result<List<TeamEntity>> {
        return try {
            delay(1000) // Simulate fetch data delay
            Result.success(teams)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}