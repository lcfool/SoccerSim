package com.soccersim.data.repositories

import com.soccersim.data.datasources.statistics.LocalStatisticsDataSource
import com.soccersim.data.datasources.statistics.RemoteStatisticsDataSource
import com.soccersim.data.models.toDomainModel
import com.soccersim.data.models.toEntity
import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.repository.StatisticsRepository

/*
* Repository should handle interactions between data and domain layers
* Theoretically we can fetch from remote and update local ds here
* Implementation details might be different based on the requirements
* */
class StatisticsRepositoryImpl(
    private val localDataSource: LocalStatisticsDataSource,
    private val remoteDataSource: RemoteStatisticsDataSource
) : StatisticsRepository {
    override suspend fun getGroupStageStatistics(): Result<List<GroupStageStatistics>> {
        // Theoretically we can fetch from remote and update local ds here
        // Implementation details might be different based on the requirements
        return localDataSource.getStatistics().map { list -> list.map { it.toDomainModel() } }
    }

    override suspend fun updateStatistics(statistics: List<GroupStageStatistics>): Result<Unit> {
        return localDataSource.saveStatistics(statistics.map { it.toEntity() })
    }
}