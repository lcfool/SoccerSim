package com.soccersim.domain.usecases.matches


import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Round
import kotlinx.coroutines.delay
import org.apache.commons.math3.random.RandomDataGenerator
import kotlin.math.max

/*
* This use case is the core one for simulating matches, because it contains logic to simulate goals
* based on the strength of the teams in match
* It uses gaussian distribution based on team strength to ensure more frequent wins of stronger team
* There's a related `Thousand rounds simulation` unit test
* that shows that stronger teams win more often
* */
class SimulateMatchesUseCase(
    private val randomDataGenerator: RandomDataGenerator = RandomDataGenerator()
) {

    suspend operator fun invoke(rounds: List<Round>): Result<List<Match>> {
        val matchesToSimulate = rounds.flatMap { listOf(it.firstMatch, it.secondMatch) }
        val simulatedMatches = mutableListOf<Match>()
        matchesToSimulate.forEach {
            simulatedMatches.add(simulateMatch(it))
        }
        return Result.success(simulatedMatches)
    }

    private suspend fun simulateMatch(match: Match): Match {
        delay(200)

        val goalsTeamA = generateGoals(match.teamA.strength)
        val goalsTeamB = generateGoals(match.teamB.strength)

        val simulatedMatch = match.copy(
            goalsTeamA = goalsTeamA,
            goalsTeamB = goalsTeamB,
            isComplete = true
        )

        return simulatedMatch
    }

    private fun generateGoals(strength: Int): Int {
        val meanGoals = strength.toDouble() / 2
        val variance = 1.5
        val randomValue = randomDataGenerator.nextGaussian(meanGoals, variance)
        return max(0, randomValue.toInt())
    }
}