package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.repository.MatchesRepository

class SaveMatchesUseCase(private val matchesRepository: MatchesRepository) {
    suspend operator fun invoke(newMatches: List<Match>): Result<Unit> {
        return matchesRepository.saveMatches(newMatches)
    }
}