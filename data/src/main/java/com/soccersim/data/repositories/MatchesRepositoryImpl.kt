package com.soccersim.data.repositories

import com.soccersim.data.datasources.matches.MatchesDataSource
import com.soccersim.data.models.toDomainModel
import com.soccersim.data.models.toEntity
import com.soccersim.domain.models.Match
import com.soccersim.domain.repository.MatchesRepository

/*
* Repository should handle interactions between data and domain layers
* Theoretically we can fetch from remote and update local ds here
* Implementation details might be different based on the requirements
* */
class MatchesRepositoryImpl(
    private val localDataSource: MatchesDataSource,
    private val remoteDataSource: MatchesDataSource
) : MatchesRepository {
    override suspend fun getMatches(): Result<List<Match>> {
        return localDataSource.getMatches().map { list -> list.map { it.toDomainModel() } }
    }

    override suspend fun saveMatches(matches: List<Match>): Result<Unit> {
        return localDataSource.saveMatches(matches.map { it.toEntity() })
    }
}