package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.MatchesRepository
import com.soccersim.domain.repository.TeamsRepository
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GenerateMatchesUseCaseTest {

    private lateinit var teamRepository: TeamsRepository
    private lateinit var matchRepository: MatchesRepository
    private lateinit var useCase: GenerateMatchesUseCase

    @Before
    fun setUp() {
        teamRepository = mockk()
        matchRepository = mockk()
        useCase = GenerateMatchesUseCase(teamRepository, matchRepository)
    }

    @Test
    fun `invoke should return a list of generated matches successfully when there are exactly 4 teams`() = runTest {
        // Given
        val teams = listOf(
            Team(1, "Team A", 5),
            Team(2, "Team B", 4),
            Team(3, "Team C", 3),
            Team(4, "Team D", 2)
        )
        coEvery { teamRepository.getTeams() } returns Result.success(teams)
        coEvery { matchRepository.saveMatches(any()) } returns Result.success(Unit)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(6, result.getOrNull()?.size)
    }

    @Test
    fun `invoke should return a failure result when teamRepository getTeams fails`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        coEvery { teamRepository.getTeams() } returns Result.failure(exception)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return a failure result when there are not exactly 4 teams`() = runTest {
        // Given
        val teams = listOf(
            Team(1, "Team A", 5),
            Team(2, "Team B", 4),
            Team(3, "Team C", 3)
        )
        coEvery { teamRepository.getTeams() } returns Result.success(teams)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals("There must be 4 teams to generate a round-robin schedule", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should return a failure result when matchRepository saveMatches fails`() = runTest {
        // Given
        val teams = listOf(
            Team(1, "Team A", 5),
            Team(2, "Team B", 4),
            Team(3, "Team C", 3),
            Team(4, "Team D", 2)
        )
        val exception = Exception("Save Matches Exception")
        coEvery { teamRepository.getTeams() } returns Result.success(teams)
        coEvery { matchRepository.saveMatches(any()) } returns Result.failure(exception)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Save Matches Exception", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should generate correct matches for teams`() = runTest {
        // Given
        val teams = listOf(
            Team(1, "Team A", 5),
            Team(2, "Team B", 4),
            Team(3, "Team C", 3),
            Team(4, "Team D", 2)
        )
        coEvery { teamRepository.getTeams() } returns Result.success(teams)
        coEvery { matchRepository.saveMatches(any()) } returns Result.success(Unit)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        val matches = result.getOrNull()
        assertNotNull(matches)
        assertEquals(6, matches!!.size)
        assertEquals(teams[0], matches[0].teamA)
        assertEquals(teams[1], matches[0].teamB)
        assertEquals(teams[2], matches[1].teamA)
        assertEquals(teams[3], matches[1].teamB)
    }

    @Test
    fun `invoke should save correct number of matches`() = runTest {
        // Given
        val teams = listOf(
            Team(1, "Team A", 5),
            Team(2, "Team B", 4),
            Team(3, "Team C", 3),
            Team(4, "Team D", 2)
        )
        coEvery { teamRepository.getTeams() } returns Result.success(teams)
        coEvery { matchRepository.saveMatches(any()) } returns Result.success(Unit)

        val matchSlot = slot<List<Match>>()
        coEvery { matchRepository.saveMatches(capture(matchSlot)) } returns Result.success(Unit)

        // When
        val result = useCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(6, matchSlot.captured.size)
    }

    @Test
    fun `invoke should handle runtime exceptions`() = runTest {
        // Given
        val exception = RuntimeException("Runtime Exception")
        coEvery { teamRepository.getTeams() } throws exception

        // When
        val result = useCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Runtime Exception", result.exceptionOrNull()?.message)
    }
}