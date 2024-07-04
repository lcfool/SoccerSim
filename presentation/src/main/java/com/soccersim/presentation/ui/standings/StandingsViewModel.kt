package com.soccersim.presentation.ui.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soccersim.domain.models.Team
import com.soccersim.domain.models.TeamStandingInfo
import com.soccersim.domain.usecases.statistics.GenerateTeamStandingsInfoUseCase
import com.soccersim.domain.usecases.statistics.GetTeamsStatisticsUseCase
import com.soccersim.domain.usecases.teams.GetTeamsUseCase
import com.soccersim.presentation.ui.common.BaseLoadingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StandingsViewModel(
    private val getTeamsUseCase: GetTeamsUseCase,
    private val getTeamsStatisticsUseCase: GetTeamsStatisticsUseCase,
    private val generateTeamStandingsInfoUseCase: GenerateTeamStandingsInfoUseCase,
) : BaseLoadingViewModel() {

    private val _standings = MutableLiveData<List<TeamStandingInfo>>(emptyList())
    val standings: LiveData<List<TeamStandingInfo>> get() = _standings

    fun fetchTeams() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            getTeamsUseCase().onSuccess { teams ->
                fetchStatistics(teams)
            }.onFailure {
                // Handle the error, e.g., log it or show a message to the user
                _isLoading.postValue(false)
            }
        }
    }

    private suspend fun fetchStatistics(teams: List<Team>) {
        getTeamsStatisticsUseCase().onSuccess { stats ->
            _standings.postValue(generateTeamStandingsInfoUseCase(teams, stats))
        }.onFailure {
            // Handle the error, e.g., log it or show a message to the user
        }
        _isLoading.postValue(false)
    }
}