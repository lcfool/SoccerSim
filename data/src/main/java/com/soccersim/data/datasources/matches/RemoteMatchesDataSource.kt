package com.soccersim.data.datasources.matches

import com.soccersim.data.models.MatchEntity

/*
* Remote data source is usually used to handle network interactions
* I left this without implementation as there are no provided endpoints to fetch data
* */
class RemoteMatchesDataSource : MatchesDataSource {
    override suspend fun getMatches(): Result<List<MatchEntity>> {
        return Result.failure(Exception("Remote datasource is not implemented yet"))
    }

    override suspend fun saveMatches(newMatches: List<MatchEntity>): Result<Unit> {
        return Result.failure(Exception("Remote datasource is not implemented yet"))
    }
}