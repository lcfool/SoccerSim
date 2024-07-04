package com.soccersim.domain.usecases.statistics

import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.repository.StatisticsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class GetTeamsStatisticsUseCaseTest {

    private val statisticsRepository = mockk<StatisticsRepository>()
    private val getTeamsStatisticsUseCase = GetTeamsStatisticsUseCase(statisticsRepository)

    @Test
    fun `should successfully retrieve statistics`() = runTest {
        // Given
        val statistics = listOf(
            GroupStageStatistics(teamId = 1, gamesPlayed = 3, gamesWon = 2, gamesDrawn = 0, gamesLost = 1, goalsFor = 7, goalsAgainst = 4, goalDifference = 3, points = 6),
            GroupStageStatistics(teamId = 2, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 1, gamesLost = 1, goalsFor = 5, goalsAgainst = 5, goalDifference = 0, points = 4)
        )
        coEvery { statisticsRepository.getGroupStageStatistics() } returns Result.success(statistics)

        // When
        val result = getTeamsStatisticsUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(statistics, result.getOrNull())
    }

    @Test
    fun `should handle failure in retrieving statistics`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        coEvery { statisticsRepository.getGroupStageStatistics() } returns Result.failure(exception)

        // When
        val result = getTeamsStatisticsUseCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `should handle empty list of statistics`() = runTest {
        // Given
        coEvery { statisticsRepository.getGroupStageStatistics() } returns Result.success(emptyList())

        // When
        val result = getTeamsStatisticsUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }
}