package com.soccersim.domain.usecases.statistics

import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.repository.StatisticsRepository

class GetTeamsStatisticsUseCase(
    private val statisticsRepository: StatisticsRepository
) {
    suspend operator fun invoke(): Result<List<GroupStageStatistics>> {
        return statisticsRepository.getGroupStageStatistics()
    }
}