package com.soccersim.domain.models

data class TeamStandingInfo(
    val team: Team,
    val gamesPlayed: Int,
    val gamesWon: Int,
    val gamesDrawn: Int,
    val gamesLost: Int,
    val goalsFor: Int,
    val goalsAgainst: Int,
    val goalDifference: Int,
    val points: Int,
    val position: Int
)
