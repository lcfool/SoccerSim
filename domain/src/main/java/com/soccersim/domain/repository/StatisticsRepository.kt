package com.soccersim.domain.repository

import com.soccersim.domain.models.GroupStageStatistics

interface StatisticsRepository {
    suspend fun getGroupStageStatistics(): Result<List<GroupStageStatistics>>
    suspend fun updateStatistics(statistics: List<GroupStageStatistics>): Result<Unit>
}