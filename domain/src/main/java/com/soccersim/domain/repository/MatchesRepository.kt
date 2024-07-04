package com.soccersim.domain.repository

import com.soccersim.domain.models.Match

interface MatchesRepository {
    suspend fun getMatches(): Result<List<Match>>
    suspend fun saveMatches(matches: List<Match>): Result<Unit>
}