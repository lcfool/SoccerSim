package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.MatchesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class SaveMatchesUseCaseTest {

    private val matchesRepository = mockk<MatchesRepository>()
    private val saveMatchesUseCase = SaveMatchesUseCase(matchesRepository)

    private val teamA = Team(1, "Team A", 5)
    private val teamB = Team(2, "Team B", 4)
    private val teamC = Team(3, "Team C", 3)
    private val teamD = Team(4, "Team D", 2)

    private val matches = listOf(
        Match(1, teamA, teamB),
        Match(2, teamC, teamD)
    )

    @Test
    fun `invoke should save matches successfully`() = runBlocking {
        // Given
        coEvery { matchesRepository.saveMatches(matches) } returns Result.success(Unit)

        // When
        val result = saveMatchesUseCase(matches)

        // Then
        assertEquals(Result.success(Unit), result)
    }

    @Test
    fun `invoke should handle save matches failure`() = runBlocking {
        // Given
        val exception = Exception("Test Exception")
        coEvery { matchesRepository.saveMatches(matches) } returns Result.failure(exception)

        // When
        val result = saveMatchesUseCase(matches)

        // Then
        assertEquals(Result.failure<Unit>(exception), result)
    }
}