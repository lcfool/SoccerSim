package com.soccersim.presentation.di

import com.soccersim.domain.usecases.matches.GenerateMatchesUseCase
import com.soccersim.domain.usecases.matches.GetMatchesUseCase
import com.soccersim.domain.usecases.matches.GroupMatchesIntoRoundsUseCase
import com.soccersim.domain.usecases.matches.SaveMatchesUseCase
import com.soccersim.domain.usecases.matches.SimulateMatchesUseCase
import com.soccersim.domain.usecases.statistics.GenerateTeamStandingsInfoUseCase
import com.soccersim.domain.usecases.statistics.GetTeamsStatisticsUseCase
import com.soccersim.domain.usecases.statistics.UpdateTeamStatisticsUseCase
import com.soccersim.domain.usecases.teams.GetTeamsUseCase
import com.soccersim.presentation.ui.rounds.RoundsViewModel
import com.soccersim.presentation.ui.standings.StandingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        RoundsViewModel(
            getMatchesUseCase = get<GetMatchesUseCase>(),
            simulateMatchUseCase = get<SimulateMatchesUseCase>(),
            saveMatchesUseCase = get<SaveMatchesUseCase>(),
            generateMatchesUseCase = get<GenerateMatchesUseCase>(),
            groupMatchesIntoRoundsUseCase = get<GroupMatchesIntoRoundsUseCase>(),
            updateTeamStatisticsUseCase = get<UpdateTeamStatisticsUseCase>()
        )
    }

    viewModel {
        StandingsViewModel(
            getTeamsUseCase = get<GetTeamsUseCase>(),
            getTeamsStatisticsUseCase = get<GetTeamsStatisticsUseCase>(),
            generateTeamStandingsInfoUseCase = get<GenerateTeamStandingsInfoUseCase>()
        )
    }
}