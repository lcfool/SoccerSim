package com.soccersim.data.datasources.matches

import com.soccersim.data.models.MatchEntity
import kotlinx.coroutines.delay

/*
* Ideally local data source should handle DB interactions
* I used list to simplify logic, as using a database was not a requirement for this assignment
* */
class LocalMatchesDataSource : MatchesDataSource {

    private val matches = mutableListOf<MatchEntity>()

    override suspend fun getMatches(): Result<List<MatchEntity>> {
        delay(1000)
        return Result.success(matches)
    }

    override suspend fun saveMatches(newMatches: List<MatchEntity>): Result<Unit> {
        matches.clear()
        matches.addAll(newMatches)
        delay(1000)
        return Result.success(Unit)
    }
}