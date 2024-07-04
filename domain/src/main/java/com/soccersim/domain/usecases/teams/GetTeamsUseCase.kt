package com.soccersim.domain.usecases.teams

import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.TeamsRepository

class GetTeamsUseCase(private val teamsRepository: TeamsRepository) {
    suspend operator fun invoke(): Result<List<Team>> {
        return teamsRepository.getTeams()
    }
}