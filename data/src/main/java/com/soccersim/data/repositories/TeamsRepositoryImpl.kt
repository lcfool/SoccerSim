package com.soccersim.data.repositories

import com.soccersim.data.datasources.teams.TeamsDataSource
import com.soccersim.data.models.toDomainModel
import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.TeamsRepository

/*
* Repository should handle interactions between data and domain layers
* Theoretically we can fetch from remote and update local ds here
* Implementation details might be different based on the requirements
* */
class TeamsRepositoryImpl(
    private val localDataSource: TeamsDataSource,
    private val remoteDataSource: TeamsDataSource
) : TeamsRepository {

    override suspend fun getTeams(): Result<List<Team>> {
        // Theoretically we can fetch from remote and update local ds here
        // Implementation details might be different based on the requirements
        return localDataSource.getTeams().map { list -> list.map { it.toDomainModel() } }
    }
}