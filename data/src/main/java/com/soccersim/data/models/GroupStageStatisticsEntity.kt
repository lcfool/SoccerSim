package com.soccersim.data.models

data class GroupStageStatisticsEntity(
    val teamId: Int,
    var gamesPlayed: Int = 0,
    var gamesWon: Int = 0,
    var gamesDrawn: Int = 0,
    var gamesLost: Int = 0,
    var goalsFor: Int = 0,
    var goalsAgainst: Int = 0,
    var goalDifference: Int = 0,
    var points: Int = 0
)