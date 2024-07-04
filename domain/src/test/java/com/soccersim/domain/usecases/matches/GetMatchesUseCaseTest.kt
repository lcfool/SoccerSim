package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.MatchesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetMatchesUseCaseTest {

    private lateinit var matchesRepository: MatchesRepository
    private lateinit var getMatchesUseCase: GetMatchesUseCase

    @Before
    fun setUp() {
        matchesRepository = mockk()
        getMatchesUseCase = GetMatchesUseCase(matchesRepository)
    }

    @Test
    fun `invoke should return a list of matches successfully when repository returns matches`() = runTest {
        // Given
        val matches = listOf(
            Match(1, Team(1, "Team A", 5), Team(2, "Team B", 4)),
            Match(2, Team(3, "Team C", 3), Team(4, "Team D", 2))
        )
        coEvery { matchesRepository.getMatches() } returns Result.success(matches)

        // When
        val result = getMatchesUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(matches, result.getOrNull())
    }

    @Test
    fun `invoke should return a failure result when repository returns failure`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        coEvery { matchesRepository.getMatches() } returns Result.failure(exception)

        // When
        val result = getMatchesUseCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke should handle an empty list of matches correctly when repository returns an empty list`() = runTest {
        // Given
        coEvery { matchesRepository.getMatches() } returns Result.success(emptyList())

        // When
        val result = getMatchesUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull().isNullOrEmpty())
    }
}