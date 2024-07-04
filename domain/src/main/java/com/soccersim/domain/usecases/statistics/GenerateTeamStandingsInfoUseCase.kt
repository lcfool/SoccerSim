package com.soccersim.domain.usecases.statistics

import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.models.Team
import com.soccersim.domain.models.TeamStandingInfo

/*
* This use case is the core one, because it contains logic to generate standings info
* based on the provided statistics of the matches played by the teams
* It uses general rules for sorting teams in the standings:
* Points
* Goal difference
* Goals for
* Goals against
* Head 2 head result in case of a tie
* It's possible to add enhanced logic here that will take into account like
* other h2h matches, fair play points etc.
* */
class GenerateTeamStandingsInfoUseCase {

    operator fun invoke(
        teams: List<Team>,
        statistics: List<GroupStageStatistics>
    ): List<TeamStandingInfo> {

        val statsWithTeams = statistics.map { stats ->
            stats to teams.find { it.id == stats.teamId }!!
        }

        var sortedStats = statsWithTeams.sortedWith(
            compareByDescending<Pair<GroupStageStatistics, Team>> { it.first.points }
                .thenByDescending { it.first.goalDifference }
                .thenByDescending { it.first.goalsFor }
                .thenByDescending { it.first.goalsAgainst }
        )

        sortedStats = resolveSortedTiesWithHeadToHeadResults(sortedStats)

        return sortedStats.mapIndexed { index, pair ->
            val (stats, team) = pair
            TeamStandingInfo(
                team = team,
                gamesPlayed = stats.gamesPlayed,
                gamesWon = stats.gamesWon,
                gamesDrawn = stats.gamesDrawn,
                gamesLost = stats.gamesLost,
                goalsFor = stats.goalsFor,
                goalsAgainst = stats.goalsAgainst,
                goalDifference = stats.goalDifference,
                points = stats.points,
                position = index + 1
            )
        }
    }

    private fun resolveSortedTiesWithHeadToHeadResults(
        sortedStats: List<Pair<GroupStageStatistics, Team>>
    ): List<Pair<GroupStageStatistics, Team>> {
        val mutableList = sortedStats.toMutableList()
        for (i in 0 until mutableList.size - 2) {
            val current = mutableList[i]
            val next = mutableList[i + 1]

            if (current.first.points == next.first.points &&
                current.first.goalDifference == next.first.goalDifference &&
                current.first.goalsFor == next.first.goalsFor &&
                current.first.goalsAgainst == next.first.goalsAgainst
            ) {
                val h2hMatch = current.first.matchHistory.find {
                    it in next.first.matchHistory
                } ?: continue

                when {
                    (h2hMatch.teamA.id == current.second.id && h2hMatch.goalsTeamA > h2hMatch.goalsTeamB) ||
                            (h2hMatch.teamB.id == current.second.id && h2hMatch.goalsTeamB > h2hMatch.goalsTeamA) -> {
                        // 'current' wins, do nothing
                    }

                    (h2hMatch.teamA.id == next.second.id && h2hMatch.goalsTeamA > h2hMatch.goalsTeamB) ||
                            (h2hMatch.teamB.id == next.second.id && h2hMatch.goalsTeamB > h2hMatch.goalsTeamA) -> {
                        // 'next' wins, swap current and next
                        mutableList[i] = next
                        mutableList[i + 1] = current
                    }
                    // Other case is a tie, do nothing or possibly implement other rules for resolving
                }

            }
        }

        return mutableList
    }
}