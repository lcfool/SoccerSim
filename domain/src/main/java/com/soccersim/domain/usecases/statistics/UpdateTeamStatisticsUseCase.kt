package com.soccersim.domain.usecases.statistics

import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.models.Match
import com.soccersim.domain.repository.StatisticsRepository

class UpdateTeamStatisticsUseCase(private val statisticsRepository: StatisticsRepository) {
    suspend operator fun invoke(matches: List<Match>): Result<Unit> {
        val teamStatsMap = mutableMapOf<Int, GroupStageStatistics>()

        matches.forEach { match ->
            val teamAStats =
                teamStatsMap.getOrPut(match.teamA.id) { GroupStageStatistics(match.teamA.id) }
            val teamBStats =
                teamStatsMap.getOrPut(match.teamB.id) { GroupStageStatistics(match.teamB.id) }

            if (match.isComplete) {
                teamAStats.gamesPlayed++
                teamBStats.gamesPlayed++

                teamAStats.goalsFor += match.goalsTeamA
                teamAStats.goalsAgainst += match.goalsTeamB

                teamBStats.goalsFor += match.goalsTeamB
                teamBStats.goalsAgainst += match.goalsTeamA

                when {
                    match.goalsTeamA > match.goalsTeamB -> {
                        teamAStats.gamesWon++
                        teamBStats.gamesLost++
                        teamAStats.points += 3
                    }

                    match.goalsTeamA < match.goalsTeamB -> {
                        teamAStats.gamesLost++
                        teamBStats.gamesWon++
                        teamBStats.points += 3
                    }

                    else -> {
                        teamAStats.gamesDrawn++
                        teamBStats.gamesDrawn++
                        teamAStats.points += 1
                        teamBStats.points += 1
                    }
                }

                teamAStats.goalDifference = teamAStats.goalsFor - teamAStats.goalsAgainst
                teamBStats.goalDifference = teamBStats.goalsFor - teamBStats.goalsAgainst

                teamAStats.matchHistory.add(match)
                teamBStats.matchHistory.add(match)
            }
        }

        return statisticsRepository.updateStatistics(teamStatsMap.values.toList())
    }
}