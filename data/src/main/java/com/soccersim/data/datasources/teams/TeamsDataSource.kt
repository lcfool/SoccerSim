package com.soccersim.data.datasources.teams

import com.soccersim.data.models.TeamEntity

interface TeamsDataSource {
    suspend fun getTeams(): Result<List<TeamEntity>>
}