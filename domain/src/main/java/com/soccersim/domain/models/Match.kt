package com.soccersim.domain.models

data class Match(
    val id: Int,
    val teamA: Team,
    val teamB: Team,
    val goalsTeamA: Int = 0,
    val goalsTeamB: Int = 0,
    val isComplete: Boolean = false
)