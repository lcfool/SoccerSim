package com.soccersim.domain.usecases.matches

import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Round

class GroupMatchesIntoRoundsUseCase {
    operator fun invoke(matches: List<Match>): List<Round> {
        val rounds = mutableListOf<Round>()
        val matchPerRound = 2
        for (i in matches.indices step matchPerRound) {
            if (i + 1 < matches.size) {
                rounds.add(Round(matches[i], matches[i + 1]))
            }
        }
        return rounds
    }
}