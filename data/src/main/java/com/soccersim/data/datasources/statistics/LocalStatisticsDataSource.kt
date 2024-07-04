package com.soccersim.data.datasources.statistics

import com.soccersim.data.models.GroupStageStatisticsEntity

/*
* Ideally local data source should handle DB interactions
* I used list to simplify logic, as using a database was not a requirement for this assignment
* */
class LocalStatisticsDataSource : StatisticsDataSource {

    private val statistics = mutableListOf<GroupStageStatisticsEntity>()

    override suspend fun getStatistics(): Result<List<GroupStageStatisticsEntity>> {
        return Result.success(statistics)
    }

    override suspend fun saveStatistics(newStatistics: List<GroupStageStatisticsEntity>): Result<Unit> {
        statistics.clear()
        statistics.addAll(newStatistics)
        return Result.success(Unit)
    }
}