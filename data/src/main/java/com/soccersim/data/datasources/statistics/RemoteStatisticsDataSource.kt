package com.soccersim.data.datasources.statistics

import com.soccersim.data.models.GroupStageStatisticsEntity

/*
* Remote data source is usually used to handle network interactions
* I left this without implementation as there are no provided endpoints to fetch data
* */
class RemoteStatisticsDataSource : StatisticsDataSource {
    override suspend fun getStatistics(): Result<List<GroupStageStatisticsEntity>> {
        return Result.failure(Exception("Remote datasource is not implemented yet"))
    }

    override suspend fun saveStatistics(newStatistics: List<GroupStageStatisticsEntity>): Result<Unit> {
        return Result.failure(Exception("Remote datasource is not implemented yet"))
    }
}