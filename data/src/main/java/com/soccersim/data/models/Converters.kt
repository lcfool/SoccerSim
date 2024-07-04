package com.soccersim.data.models

import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Team

fun Team.toEntity(): TeamEntity {
    return TeamEntity(
        id = this.id,
        name = this.name,
        strength = this.strength
    )
}

fun Match.toEntity(): MatchEntity {
    return MatchEntity(
        id = this.id,
        teamA = this.teamA.toEntity(),
        teamB = this.teamB.toEntity(),
        goalsTeamA = this.goalsTeamA,
        goalsTeamB = this.goalsTeamB
    )
}

fun GroupStageStatistics.toEntity(): GroupStageStatisticsEntity {
    return GroupStageStatisticsEntity(
        teamId = teamId,
        gamesPlayed = gamesPlayed,
        gamesWon = gamesWon,
        gamesDrawn = gamesDrawn,
        gamesLost = gamesLost,
        goalsFor = goalsFor,
        goalsAgainst = goalsAgainst,
        goalDifference = goalDifference,
        points = points
    )
}

fun TeamEntity.toDomainModel(): Team {
    return Team(
        id = this.id,
        name = this.name,
        strength = this.strength
    )
}

fun MatchEntity.toDomainModel(): Match {
    return Match(
        id = this.id,
        teamA = this.teamA.toDomainModel(),
        teamB = this.teamB.toDomainModel(),
        goalsTeamA = this.goalsTeamA,
        goalsTeamB = this.goalsTeamB
    )
}

fun GroupStageStatisticsEntity.toDomainModel(): GroupStageStatistics {
    return GroupStageStatistics(
        teamId = teamId,
        gamesPlayed = gamesPlayed,
        gamesWon = gamesWon,
        gamesDrawn = gamesDrawn,
        gamesLost = gamesLost,
        goalsFor = goalsFor,
        goalsAgainst = goalsAgainst,
        goalDifference = goalDifference,
        points = points
    )
}