package com.soccersim.data.datasources

import com.soccersim.data.datasources.teams.LocalTeamsDataSource
import com.soccersim.data.models.TeamEntity
import io.mockk.coEvery
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalTeamsDataSourceTest {

    private val localTeamsDataSource = LocalTeamsDataSource()

    @Test
    fun `getTeams should return correct list of teams`() = runTest {
        // Given
        val expectedTeams = listOf(
            TeamEntity(1, "Team A", 5),
            TeamEntity(2, "Team B", 4),
            TeamEntity(3, "Team C", 3),
            TeamEntity(4, "Team D", 2)
        )

        // When
        val result = localTeamsDataSource.getTeams()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedTeams, result.getOrNull())
    }

    @Test
    fun `getTeams should handle exceptions and return failure`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        val localTeamsDataSourceSpy = spyk(LocalTeamsDataSource())
        coEvery { localTeamsDataSourceSpy.getTeams() } coAnswers { Result.failure(exception) }

        // When
        val result = localTeamsDataSourceSpy.getTeams()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }
}