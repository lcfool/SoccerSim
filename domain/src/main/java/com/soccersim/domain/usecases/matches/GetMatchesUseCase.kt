package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.repository.MatchesRepository

class GetMatchesUseCase(private val matchesRepository: MatchesRepository) {
    suspend operator fun invoke(): Result<List<Match>> {
        return matchesRepository.getMatches()
    }
}