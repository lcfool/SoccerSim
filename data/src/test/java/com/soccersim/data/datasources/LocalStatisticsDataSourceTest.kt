package com.soccersim.data.datasources

import com.soccersim.data.datasources.statistics.LocalStatisticsDataSource
import com.soccersim.data.models.GroupStageStatisticsEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalStatisticsDataSourceTest {

    private lateinit var localStatisticsDataSource: LocalStatisticsDataSource

    @Before
    fun setUp() {
        localStatisticsDataSource = LocalStatisticsDataSource()
    }

    @Test
    fun `getStatistics should return the current list of statistics`() = runTest {
        // Given
        val expectedStatistics = listOf(
            GroupStageStatisticsEntity(teamId = 1, gamesPlayed = 10, gamesWon = 6, gamesDrawn = 2, gamesLost = 2, goalsFor = 18, goalsAgainst = 8, goalDifference = 10, points = 20),
            GroupStageStatisticsEntity(teamId = 2, gamesPlayed = 10, gamesWon = 5, gamesDrawn = 3, gamesLost = 2, goalsFor = 15, goalsAgainst = 10, goalDifference = 5, points = 18)
        )
        localStatisticsDataSource.saveStatistics(expectedStatistics)

        // When
        val result = localStatisticsDataSource.getStatistics()

        // Then
        assertEquals(Result.success(expectedStatistics), result)
    }

    @Test
    fun `getStatistics should return empty list when no statistics are saved`() = runTest {
        // When
        val result = localStatisticsDataSource.getStatistics()

        // Then
        assertEquals(Result.success(emptyList<GroupStageStatisticsEntity>()), result)
    }

    @Test
    fun `saveStatistics should update the statistics list`() = runTest {
        // Given
        val initialStatistics = listOf(
            GroupStageStatisticsEntity(teamId = 1, gamesPlayed = 10, gamesWon = 6, gamesDrawn = 2, gamesLost = 2, goalsFor = 18, goalsAgainst = 8, goalDifference = 10, points = 20)
        )
        localStatisticsDataSource.saveStatistics(initialStatistics)

        val newStatistics = listOf(
            GroupStageStatisticsEntity(teamId = 2, gamesPlayed = 10, gamesWon = 5, gamesDrawn = 3, gamesLost = 2, goalsFor = 15, goalsAgainst = 10, goalDifference = 5, points = 18),
            GroupStageStatisticsEntity(teamId = 3, gamesPlayed = 10, gamesWon = 7, gamesDrawn = 2, gamesLost = 1, goalsFor = 20, goalsAgainst = 6, goalDifference = 14, points = 23)
        )

        // When
        val saveResult = localStatisticsDataSource.saveStatistics(newStatistics)
        val getResult = localStatisticsDataSource.getStatistics()

        // Then
        assertEquals(Result.success(Unit), saveResult)
        assertEquals(Result.success(newStatistics), getResult)
    }

    @Test
    fun `saveStatistics with empty list should clear the statistics list`() = runTest {
        // Given
        val initialStatistics = listOf(
            GroupStageStatisticsEntity(teamId = 1, gamesPlayed = 10, gamesWon = 6, gamesDrawn = 2, gamesLost = 2, goalsFor = 18, goalsAgainst = 8, goalDifference = 10, points = 20)
        )
        localStatisticsDataSource.saveStatistics(initialStatistics)

        // When
        val saveResult = localStatisticsDataSource.saveStatistics(emptyList())
        val getResult = localStatisticsDataSource.getStatistics()

        // Then
        assertEquals(Result.success(Unit), saveResult)
        assertEquals(Result.success(emptyList<GroupStageStatisticsEntity>()), getResult)
    }

    @Test
    fun `concurrent access to saveStatistics and getStatistics should be thread-safe`() = runTest {
        // Given
        val statisticsList1 = listOf(
            GroupStageStatisticsEntity(teamId = 1, gamesPlayed = 10, gamesWon = 6, gamesDrawn = 2, gamesLost = 2, goalsFor = 18, goalsAgainst = 8, goalDifference = 10, points = 20)
        )
        val statisticsList2 = listOf(
            GroupStageStatisticsEntity(teamId = 2, gamesPlayed = 8, gamesWon = 5, gamesDrawn = 1, gamesLost = 2, goalsFor = 15, goalsAgainst = 10, goalDifference = 5, points = 16)
        )

        // When
        val job1 = launch { localStatisticsDataSource.saveStatistics(statisticsList1) }
        val job2 = launch { localStatisticsDataSource.saveStatistics(statisticsList2) }
        joinAll(job1, job2)
        val result = localStatisticsDataSource.getStatistics()

        // Then
        // Ensure that one of the lists is saved and returned
        assert(result.isSuccess)
        assert(result.getOrThrow() in listOf(statisticsList1, statisticsList2))
    }
}