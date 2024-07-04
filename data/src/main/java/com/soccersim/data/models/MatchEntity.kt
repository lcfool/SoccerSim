package com.soccersim.data.models

data class MatchEntity(
    val id: Int,
    val teamA: TeamEntity,
    val teamB: TeamEntity,
    var goalsTeamA: Int = 0,
    var goalsTeamB: Int = 0
)