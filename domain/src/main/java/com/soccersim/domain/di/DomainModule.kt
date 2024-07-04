package com.soccersim.domain.di

import com.soccersim.domain.repository.MatchesRepository
import com.soccersim.domain.repository.StatisticsRepository
import com.soccersim.domain.repository.TeamsRepository
import com.soccersim.domain.usecases.matches.GenerateMatchesUseCase
import com.soccersim.domain.usecases.matches.GetMatchesUseCase
import com.soccersim.domain.usecases.matches.GroupMatchesIntoRoundsUseCase
import com.soccersim.domain.usecases.matches.SaveMatchesUseCase
import com.soccersim.domain.usecases.matches.SimulateMatchesUseCase
import com.soccersim.domain.usecases.statistics.GenerateTeamStandingsInfoUseCase
import com.soccersim.domain.usecases.statistics.GetTeamsStatisticsUseCase
import com.soccersim.domain.usecases.statistics.UpdateTeamStatisticsUseCase
import com.soccersim.domain.usecases.teams.GetTeamsUseCase
import org.apache.commons.math3.random.RandomDataGenerator
import org.koin.dsl.module

val domainModule = module {
    factory { GetTeamsUseCase(get<TeamsRepository>()) }
    factory { GetMatchesUseCase(get<MatchesRepository>()) }
    factory { SaveMatchesUseCase(get<MatchesRepository>()) }
    factory { SimulateMatchesUseCase(get<RandomDataGenerator>()) }
    factory { GenerateMatchesUseCase(get<TeamsRepository>(), get<MatchesRepository>()) }
    factory { GroupMatchesIntoRoundsUseCase() }
    factory { GenerateTeamStandingsInfoUseCase() }
    factory { GetTeamsStatisticsUseCase(get<StatisticsRepository>()) }
    factory { UpdateTeamStatisticsUseCase(get<StatisticsRepository>()) }

    factory { RandomDataGenerator() }
}