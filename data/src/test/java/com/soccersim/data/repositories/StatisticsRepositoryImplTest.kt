package com.soccersim.data.repositories

import com.soccersim.data.datasources.statistics.LocalStatisticsDataSource
import com.soccersim.data.datasources.statistics.RemoteStatisticsDataSource
import com.soccersim.data.models.GroupStageStatisticsEntity
import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.repository.StatisticsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsRepositoryImplTest {

    private lateinit var localDataSource: LocalStatisticsDataSource
    private lateinit var remoteDataSource: RemoteStatisticsDataSource
    private lateinit var repository: StatisticsRepository

    @Before
    fun setUp() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        repository = StatisticsRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `getGroupStageStatistics should return a list of statistics successfully`() = runTest {
        // Given
        val statisticsEntities = listOf(
            GroupStageStatisticsEntity(teamId = 1, gamesPlayed = 3, gamesWon = 2, gamesDrawn = 0, gamesLost = 1, goalsFor = 5, goalsAgainst = 3, goalDifference = 2, points = 6),
            GroupStageStatisticsEntity(teamId = 2, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 1, gamesLost = 1, goalsFor = 4, goalsAgainst = 4, goalDifference = 0, points = 4)
        )
        coEvery { localDataSource.getStatistics() } returns Result.success(statisticsEntities)

        // When
        val result = repository.getGroupStageStatistics()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getGroupStageStatistics should return a failure result when localDataSource fails`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        coEvery { localDataSource.getStatistics() } returns Result.failure(exception)

        // When
        val result = repository.getGroupStageStatistics()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }

    @Test
    fun `updateStatistics should update statistics successfully`() = runTest {
        // Given
        val statistics = listOf(
            GroupStageStatistics(teamId = 1, gamesPlayed = 3, gamesWon = 2, gamesDrawn = 0, gamesLost = 1, goalsFor = 5, goalsAgainst = 3, goalDifference = 2, points = 6),
            GroupStageStatistics(teamId = 2, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 1, gamesLost = 1, goalsFor = 4, goalsAgainst = 4, goalDifference = 0, points = 4)
        )
        coEvery { localDataSource.saveStatistics(any()) } returns Result.success(Unit)

        // When
        val result = repository.updateStatistics(statistics)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `updateStatistics should return a failure result when localDataSource fails`() = runTest {
        // Given
        val statistics = listOf(
            GroupStageStatistics(teamId = 1, gamesPlayed = 3, gamesWon = 2, gamesDrawn = 0, gamesLost = 1, goalsFor = 5, goalsAgainst = 3, goalDifference = 2, points = 6),
            GroupStageStatistics(teamId = 2, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 1, gamesLost = 1, goalsFor = 4, goalsAgainst = 4, goalDifference = 0, points = 4)
        )
        val exception = Exception("Test Exception")
        coEvery { localDataSource.saveStatistics(any()) } returns Result.failure(exception)

        // When
        val result = repository.updateStatistics(statistics)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }
}