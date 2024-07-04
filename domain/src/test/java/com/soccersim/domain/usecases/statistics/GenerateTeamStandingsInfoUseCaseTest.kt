package com.soccersim.domain.usecases.statistics

import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Team
import com.soccersim.domain.models.TeamStandingInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class GenerateTeamStandingsInfoUseCaseTest {

    private val teamA = Team(1, "Team A", 5)
    private val teamB = Team(2, "Team B", 4)
    private val teamC = Team(3, "Team C", 3)
    private val teamD = Team(4, "Team D", 2)
    private val teams = listOf(teamA, teamB, teamC, teamD)

    private val statistics = listOf(
        GroupStageStatistics(1, 3, 2, 0, 1, 7, 4, 3, 6, mutableListOf(Match(1, teamA, teamB, 3, 1, true))),
        GroupStageStatistics(2, 3, 2, 0, 1, 6, 5, 1, 6, mutableListOf(Match(1, teamA, teamB, 3, 1, true))),
        GroupStageStatistics(3, 3, 1, 0, 2, 4, 6, -2, 3, mutableListOf(Match(2, teamC, teamD, 2, 1, true))),
        GroupStageStatistics(4, 3, 1, 0, 2, 4, 6, -2, 3, mutableListOf(Match(2, teamC, teamD, 2, 1, true)))
    )

    private val generateTeamStandingsInfoUseCase = GenerateTeamStandingsInfoUseCase()

    @Test
    fun `should return empty list for empty teams and statistics`() {
        // Given
        val teams = emptyList<Team>()
        val statistics = emptyList<GroupStageStatistics>()

        // When
        val result = generateTeamStandingsInfoUseCase(teams, statistics)

        // Then
        assertEquals(emptyList<TeamStandingInfo>(), result)
    }

    @Test
    fun `should return correct standings for single team and statistics`() {
        // Given
        val singleTeam = listOf(teamA)
        val singleStat = listOf(GroupStageStatistics(1, 1, 1, 0, 0, 5, 2, 3, 3))

        // When
        val result = generateTeamStandingsInfoUseCase(singleTeam, singleStat)

        // Then
        val expected = listOf(
            TeamStandingInfo(team = teamA, gamesPlayed = 1, gamesWon = 1, gamesDrawn = 0, gamesLost = 0, goalsFor = 5, goalsAgainst = 2, goalDifference = 3, points = 3, position = 1)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should return correct standings for multiple teams and statistics with no ties`() {
        // Given

        // When
        val result = generateTeamStandingsInfoUseCase(teams, statistics)

        // Then
        val expected = listOf(
            TeamStandingInfo(team = teamA, gamesPlayed = 3, gamesWon = 2, gamesDrawn = 0, gamesLost = 1, goalsFor = 7, goalsAgainst = 4, goalDifference = 3, points = 6, position = 1),
            TeamStandingInfo(team = teamB, gamesPlayed = 3, gamesWon = 2, gamesDrawn = 0, gamesLost = 1, goalsFor = 6, goalsAgainst = 5, goalDifference = 1, points = 6, position = 2),
            TeamStandingInfo(team = teamC, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 0, gamesLost = 2, goalsFor = 4, goalsAgainst = 6, goalDifference = -2, points = 3, position = 3),
            TeamStandingInfo(team = teamD, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 0, gamesLost = 2, goalsFor = 4, goalsAgainst = 6, goalDifference = -2, points = 3, position = 4)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should handle ties with head-to-head results`() {
        // Given

        // When
        val result = generateTeamStandingsInfoUseCase(teams, statistics)

        // Then
        val expected = listOf(
            TeamStandingInfo(team = teamA, gamesPlayed = 3, gamesWon = 2, gamesDrawn = 0, gamesLost = 1, goalsFor = 7, goalsAgainst = 4, goalDifference = 3, points = 6, position = 1),
            TeamStandingInfo(team = teamB, gamesPlayed = 3, gamesWon = 2, gamesDrawn = 0, gamesLost = 1, goalsFor = 6, goalsAgainst = 5, goalDifference = 1, points = 6, position = 2),
            TeamStandingInfo(team = teamC, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 0, gamesLost = 2, goalsFor = 4, goalsAgainst = 6, goalDifference = -2, points = 3, position = 3),
            TeamStandingInfo(team = teamD, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 0, gamesLost = 2, goalsFor = 4, goalsAgainst = 6, goalDifference = -2, points = 3, position = 4)
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should handle multiple tie breaks correctly`() {
        // Given
        val tieStats = listOf(
            GroupStageStatistics(1, 3, 1, 1, 1, 5, 5, 0, 4, mutableListOf(Match(1, teamA, teamB, 2, 2, true))),
            GroupStageStatistics(2, 3, 1, 1, 1, 5, 5, 0, 4, mutableListOf(Match(1, teamA, teamB, 2, 2, true))),
            GroupStageStatistics(3, 3, 1, 1, 1, 5, 5, 0, 4, mutableListOf(Match(3, teamC, teamD, 2, 2, true))),
            GroupStageStatistics(4, 3, 1, 1, 1, 5, 5, 0, 4, mutableListOf(Match(3, teamC, teamD, 2, 2, true)))
        )

        // When
        val result = generateTeamStandingsInfoUseCase(teams, tieStats)

        // Then
        val expected = listOf(
            TeamStandingInfo(team = teamA, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 1, gamesLost = 1, goalsFor = 5, goalsAgainst = 5, goalDifference = 0, points = 4, position = 1),
            TeamStandingInfo(team = teamB, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 1, gamesLost = 1, goalsFor = 5, goalsAgainst = 5, goalDifference = 0, points = 4, position = 2),
            TeamStandingInfo(team = teamC, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 1, gamesLost = 1, goalsFor = 5, goalsAgainst = 5, goalDifference = 0, points = 4, position = 3),
            TeamStandingInfo(team = teamD, gamesPlayed = 3, gamesWon = 1, gamesDrawn = 1, gamesLost = 1, goalsFor = 5, goalsAgainst = 5, goalDifference = 0, points = 4, position = 4)
        )
        assertEquals(expected, result)
    }
}