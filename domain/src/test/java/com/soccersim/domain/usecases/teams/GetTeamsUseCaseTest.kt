package com.soccersim.domain.usecases.teams

import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.TeamsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class GetTeamsUseCaseTest {

    private val teamsRepository = mockk<TeamsRepository>()
    private val getTeamsUseCase = GetTeamsUseCase(teamsRepository)

    private val teamA = Team(id = 1, name = "Team A", strength = 5)
    private val teamB = Team(id = 2, name = "Team B", strength = 4)

    @Test
    fun `should successfully retrieve teams`() = runTest {
        // Given
        val teams = listOf(teamA, teamB)
        coEvery { teamsRepository.getTeams() } returns Result.success(teams)

        // When
        val result = getTeamsUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(teams, result.getOrNull())
        coVerify { teamsRepository.getTeams() }
    }

    @Test
    fun `should handle repository failure`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        coEvery { teamsRepository.getTeams() } returns Result.failure(exception)

        // When
        val result = getTeamsUseCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        coVerify { teamsRepository.getTeams() }
    }
}