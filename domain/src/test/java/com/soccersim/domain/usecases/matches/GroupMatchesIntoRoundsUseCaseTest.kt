package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Round
import com.soccersim.domain.models.Team
import org.junit.Assert.assertEquals
import org.junit.Test

class GroupMatchesIntoRoundsUseCaseTest {

    private val groupMatchesIntoRoundsUseCase = GroupMatchesIntoRoundsUseCase()

    private val teamA = Team(1, "Team A", 5)
    private val teamB = Team(2, "Team B", 4)
    private val teamC = Team(3, "Team C", 3)
    private val teamD = Team(4, "Team D", 2)

    @Test
    fun `group matches into rounds correctly with 4 matches`() {
        // Given
        val matches = listOf(
            Match(1, teamA, teamB),
            Match(2, teamC, teamD),
            Match(3, teamA, teamC),
            Match(4, teamB, teamD)
        )

        // When
        val result = groupMatchesIntoRoundsUseCase(matches)

        // Then
        val expectedRounds = listOf(
            Round(matches[0], matches[1]),
            Round(matches[2], matches[3])
        )
        assertEquals(expectedRounds, result)
    }

    @Test
    fun `handle odd number of matches correctly`() {
        // Given
        val matches = listOf(
            Match(1, teamA, teamB),
            Match(2, teamC, teamD),
            Match(3, teamA, teamC)
        )

        // When
        val result = groupMatchesIntoRoundsUseCase(matches)

        // Then
        val expectedRounds = listOf(
            Round(matches[0], matches[1])
        )
        assertEquals(expectedRounds, result)
    }

    @Test
    fun `handle no matches correctly`() {
        // Given
        val matches = emptyList<Match>()

        // When
        val result = groupMatchesIntoRoundsUseCase(matches)

        // Then
        val expectedRounds = emptyList<Round>()
        assertEquals(expectedRounds, result)
    }

    @Test
    fun `handle single match correctly`() {
        // Given
        val matches = listOf(
            Match(1, teamA, teamB)
        )

        // When
        val result = groupMatchesIntoRoundsUseCase(matches)

        // Then
        val expectedRounds = emptyList<Round>()
        assertEquals(expectedRounds, result)
    }

    @Test
    fun `handle multiple rounds correctly with 6 matches`() {
        // Given
        val matches = listOf(
            Match(1, teamA, teamB),
            Match(2, teamC, teamD),
            Match(3, teamA, teamC),
            Match(4, teamB, teamD),
            Match(5, teamA, teamD),
            Match(6, teamB, teamC)
        )

        // When
        val result = groupMatchesIntoRoundsUseCase(matches)

        // Then
        val expectedRounds = listOf(
            Round(matches[0], matches[1]),
            Round(matches[2], matches[3]),
            Round(matches[4], matches[5])
        )
        assertEquals(expectedRounds, result)
    }

    @Test
    fun `handle large number of matches correctly`() {
        // Given
        val matches = (1..1000).map {
            if (it % 2 == 0) {
                Match(it, teamA, teamB)
            } else {
                Match(it, teamC, teamD)
            }
        }

        // When
        val result = groupMatchesIntoRoundsUseCase(matches)

        // Then
        val expectedRounds = (1..500).map {
            Round(matches[2 * (it - 1)], matches[2 * (it - 1) + 1])
        }
        assertEquals(expectedRounds, result)
    }
}