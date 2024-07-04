package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Round
import com.soccersim.domain.models.Team
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.apache.commons.math3.random.RandomDataGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class SimulateMatchesUseCaseTest {

    private val randomDataGeneratorMock = mockk<RandomDataGenerator>()
        private val teamA = Team(1, "Team A", 5)
    private val teamB = Team(2, "Team B", 4)
    private val teamC = Team(3, "Team C", 3)
    private val teamD = Team(4, "Team D", 2)

    private val match1 = Match(1, teamA, teamB)
    private val match2 = Match(2, teamC, teamD)
    private val round = Round(match1, match2)
    private val rounds = listOf(round)

    @Test
    fun `invoke should simulate matches successfully`() = runTest {
        // Given
        val simulateMatchesUseCase = SimulateMatchesUseCase(randomDataGeneratorMock)
        every { randomDataGeneratorMock.nextGaussian(any(), any()) } returns 2.0

        // When
        val result = simulateMatchesUseCase(rounds)

        // Then
        assert(result.isSuccess)
        val simulatedMatches = result.getOrNull()
        assertEquals(2, simulatedMatches?.size)
        assertEquals(2, simulatedMatches?.first()?.goalsTeamA)
        assertEquals(2, simulatedMatches?.first()?.goalsTeamB)
        assertEquals(2, simulatedMatches?.last()?.goalsTeamA)
        assertEquals(2, simulatedMatches?.last()?.goalsTeamB)
    }

    @Test
    fun `invoke should handle no rounds provided`() = runTest {
        // Given
        val simulateMatchesUseCase = SimulateMatchesUseCase(RandomDataGenerator())

        // When
        val result = simulateMatchesUseCase(emptyList())

        // Then
        assert(result.isSuccess)
        val simulatedMatches = result.getOrNull()
        assertEquals(0, simulatedMatches?.size)
    }

    @Test
    fun `simulateMatch should generate non-negative goals`() = runTest {
        // Given
        val simulateMatchesUseCase = SimulateMatchesUseCase(RandomDataGenerator())

        // When
        val result = simulateMatchesUseCase(rounds)

        // Then
        assert(result.isSuccess)
        val simulatedMatches = result.getOrNull()
        assertTrue(simulatedMatches?.all { it.goalsTeamA >= 0 && it.goalsTeamB >= 0 } == true)
        assert((simulatedMatches?.first()?.goalsTeamA ?: -1) >= 0)
        assert((simulatedMatches?.first()?.goalsTeamB ?: -1) >= 0)
    }

    @Test
    fun `Thousand rounds simulation`() = runTest {
        // Given
        val simulateMatchesUseCase = SimulateMatchesUseCase(RandomDataGenerator())
        var strongerTeamWins = 0
        var weakerTeamWins = 0
        var draws = 0
        // When
        repeat(1000) {
            simulateMatchesUseCase(rounds).onSuccess { matchList ->
                matchList.forEach { match ->
                    when {
                        match.goalsTeamA > match.goalsTeamB -> strongerTeamWins++
                        match.goalsTeamA < match.goalsTeamB -> weakerTeamWins++
                        else -> draws++
                    }
                }
            }
        }

        println("Stronger Team Wins: $strongerTeamWins")
        println("Weaker Team Wins: $weakerTeamWins")
        println("Draws: $draws")
        assertEquals(strongerTeamWins + weakerTeamWins + draws, 2000)
        assert(strongerTeamWins > weakerTeamWins)
    }
}