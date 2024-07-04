package com.soccersim.presentation.ui.rounds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Round
import com.soccersim.domain.usecases.matches.GenerateMatchesUseCase
import com.soccersim.domain.usecases.matches.GetMatchesUseCase
import com.soccersim.domain.usecases.matches.GroupMatchesIntoRoundsUseCase
import com.soccersim.domain.usecases.matches.SaveMatchesUseCase
import com.soccersim.domain.usecases.matches.SimulateMatchesUseCase
import com.soccersim.domain.usecases.statistics.UpdateTeamStatisticsUseCase
import com.soccersim.presentation.ui.common.BaseLoadingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoundsViewModel(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val simulateMatchUseCase: SimulateMatchesUseCase,
    private val saveMatchesUseCase: SaveMatchesUseCase,
    private val generateMatchesUseCase: GenerateMatchesUseCase,
    private val groupMatchesIntoRoundsUseCase: GroupMatchesIntoRoundsUseCase,
    private val updateTeamStatisticsUseCase: UpdateTeamStatisticsUseCase
) : BaseLoadingViewModel() {

    private val roundsList = mutableListOf<Round>()

    private val _rounds = MutableLiveData<List<Round>>(emptyList())
    val rounds: LiveData<List<Round>>
        get() = _rounds


    fun fetchMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            if (roundsList.isEmpty()) {
                getMatchesUseCase().onSuccess { matchList ->
                    if (matchList.isEmpty()) {
                        generateMatches()
                    } else {
                        generateRounds(matchList)
                    }
                }.onFailure {
                    // Handle the error, e.g., log it or show a message to the user

                }
            } else {
                _rounds.postValue(roundsList.toList())
                _isLoading.postValue(false)
            }
        }
    }

    private fun generateMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            generateMatchesUseCase().onSuccess {
                fetchMatches()
            }.onFailure {
                // Handle the error, e.g., log it or show a message to the user
            }
            _isLoading.postValue(false)
        }
    }

    fun simulateMatches() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            simulateMatchUseCase.invoke(roundsList)
                .onSuccess { simulatedMatches ->
                    saveSimulatedMatches(simulatedMatches)
                }.onFailure {
                    // Handle the error, e.g., log it or show a message to the user
                    _isLoading.postValue(false)
                }
        }
    }

    private suspend fun saveSimulatedMatches(simulatedMatches: List<Match>) {
        saveMatchesUseCase(simulatedMatches)
            .onSuccess {
                updateTeamStatistics(simulatedMatches)
            }.onFailure {
                // Handle the error, e.g., log it or show a message to the user
                _isLoading.postValue(false)
            }
    }

    private suspend fun updateTeamStatistics(matches: List<Match>) {
        updateTeamStatisticsUseCase(matches).onSuccess {
            generateRounds(matches)
        }.onFailure {
            // Handle the error, e.g., log it or show a message to the user
            _isLoading.postValue(false)
        }
    }

    private fun generateRounds(matches: List<Match>) {
        roundsList.clear()
        roundsList.addAll(groupMatchesIntoRoundsUseCase(matches))
        _rounds.postValue(roundsList.toList())
        _isLoading.postValue(false)
    }
}