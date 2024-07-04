package com.soccersim.domain.usecases.statistics

import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.StatisticsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateTeamStatisticsUseCaseTest {

    private val statisticsRepository = mockk<StatisticsRepository>()
    private val updateTeamStatisticsUseCase = UpdateTeamStatisticsUseCase(statisticsRepository)

    private val teamA = Team(id = 1, name = "Team A", strength = 5)
    private val teamB = Team(id = 2, name = "Team B", strength = 4)
    private val teamC = Team(id = 3, name = "Team C", strength = 3)
    private val teamD = Team(id = 4, name = "Team D", strength = 2)

    private val match1 = Match(id = 1, teamA = teamA, teamB = teamB, goalsTeamA = 2, goalsTeamB = 1, isComplete = true)
    private val match2 = Match(id = 2, teamA = teamC, teamB = teamD, goalsTeamA = 0, goalsTeamB = 0, isComplete = true)
    private val match3 = Match(id = 3, teamA = teamA, teamB = teamC, goalsTeamA = 1, goalsTeamB = 3, isComplete = true)

    @Test
    fun `should successfully update team statistics`() = runTest {
        // Given
        coEvery { statisticsRepository.updateStatistics(any()) } returns Result.success(Unit)

        // When
        val result = updateTeamStatisticsUseCase(listOf(match1, match2, match3))

        // Then
        assertTrue(result.isSuccess)
        coVerify { statisticsRepository.updateStatistics(any()) }
    }

    @Test
    fun `should handle empty match list`() = runTest {
        // Given
        coEvery { statisticsRepository.updateStatistics(any()) } returns Result.success(Unit)

        // When
        val result = updateTeamStatisticsUseCase(emptyList())

        // Then
        assertTrue(result.isSuccess)
        coVerify { statisticsRepository.updateStatistics(emptyList()) }
    }

    @Test
    fun `should handle repository failure`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        coEvery { statisticsRepository.updateStatistics(any()) } returns Result.failure(exception)

        // When
        val result = updateTeamStatisticsUseCase(listOf(match1, match2, match3))

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}