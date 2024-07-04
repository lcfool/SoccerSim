package com.soccersim.data.datasources.statistics

import com.soccersim.data.models.GroupStageStatisticsEntity

interface StatisticsDataSource {
    suspend fun getStatistics(): Result<List<GroupStageStatisticsEntity>>
    suspend fun saveStatistics(newStatistics: List<GroupStageStatisticsEntity>): Result<Unit>
}