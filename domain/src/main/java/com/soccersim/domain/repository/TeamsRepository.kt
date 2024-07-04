package com.soccersim.domain.repository

import com.soccersim.domain.models.Team

interface TeamsRepository {
    suspend fun getTeams(): Result<List<Team>>
}