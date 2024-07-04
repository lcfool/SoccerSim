package com.soccersim.data.di

import com.soccersim.data.datasources.matches.LocalMatchesDataSource
import com.soccersim.data.datasources.matches.RemoteMatchesDataSource
import com.soccersim.data.datasources.statistics.LocalStatisticsDataSource
import com.soccersim.data.datasources.statistics.RemoteStatisticsDataSource
import com.soccersim.data.datasources.teams.LocalTeamsDataSource
import com.soccersim.data.datasources.teams.RemoteTeamsDataSource
import com.soccersim.data.repositories.MatchesRepositoryImpl
import com.soccersim.data.repositories.StatisticsRepositoryImpl
import com.soccersim.data.repositories.TeamsRepositoryImpl
import com.soccersim.domain.repository.MatchesRepository
import com.soccersim.domain.repository.StatisticsRepository
import com.soccersim.domain.repository.TeamsRepository
import org.koin.dsl.module

val dataModule = module {
    single { LocalTeamsDataSource() }
    single { RemoteTeamsDataSource() }

    single { LocalMatchesDataSource() }
    single { RemoteMatchesDataSource() }

    single { LocalStatisticsDataSource() }
    single { RemoteStatisticsDataSource() }

    single<TeamsRepository> {
        TeamsRepositoryImpl(
            localDataSource = get<LocalTeamsDataSource>(),
            remoteDataSource = get<RemoteTeamsDataSource>()
        )
    }
    single<MatchesRepository> {
        MatchesRepositoryImpl(
            localDataSource = get<LocalMatchesDataSource>(),
            remoteDataSource = get<RemoteMatchesDataSource>()
        )
    }

    single<StatisticsRepository> {
        StatisticsRepositoryImpl(
            localDataSource = get<LocalStatisticsDataSource>(),
            remoteDataSource = get<RemoteStatisticsDataSource>()
        )
    }
}