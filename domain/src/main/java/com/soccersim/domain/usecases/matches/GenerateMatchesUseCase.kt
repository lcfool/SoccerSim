package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.MatchesRepository
import com.soccersim.domain.repository.TeamsRepository

class GenerateMatchesUseCase(
    private val teamRepository: TeamsRepository,
    private val matchRepository: MatchesRepository
) {
    suspend operator fun invoke(): Result<List<Match>> {
        return try {
            val teamsResult = teamRepository.getTeams()

            if (teamsResult.isFailure) {
                return Result.failure(teamsResult.exceptionOrNull()!!)
            }

            val teams = teamsResult.getOrNull()
            if (teams == null || teams.size != 4) {
                return Result.failure(
                    Exception("There must be 4 teams to generate a round-robin schedule")
                )
            }

            val matches = generateRoundRobinMatches(teams)
            val saveMatchesResult = matchRepository.saveMatches(matches)
            return saveMatchesResult.map { matches }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun generateRoundRobinMatches(teams: List<Team>): List<Match> {
        val matchList = mutableListOf<Match>()

        matchList.add(Match(1, teams[0], teams[1]))
        matchList.add(Match(2, teams[2], teams[3]))

        matchList.add(Match(3, teams[0], teams[2]))
        matchList.add(Match(4, teams[1], teams[3]))

        matchList.add(Match(5, teams[0], teams[3]))
        matchList.add(Match(6, teams[1], teams[2]))

        return matchList
    }
}