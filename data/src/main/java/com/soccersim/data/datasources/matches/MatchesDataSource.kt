package com.soccersim.data.datasources.matches

import com.soccersim.data.models.MatchEntity

interface MatchesDataSource {
    suspend fun getMatches(): Result<List<MatchEntity>>
    suspend fun saveMatches(newMatches: List<MatchEntity>): Result<Unit>
}