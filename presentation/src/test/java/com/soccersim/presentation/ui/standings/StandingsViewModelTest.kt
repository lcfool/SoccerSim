package com.soccersim.presentation.ui.standings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.soccersim.domain.models.GroupStageStatistics
import com.soccersim.domain.models.Team
import com.soccersim.domain.models.TeamStandingInfo
import com.soccersim.domain.usecases.statistics.GenerateTeamStandingsInfoUseCase
import com.soccersim.domain.usecases.statistics.GetTeamsStatisticsUseCase
import com.soccersim.domain.usecases.teams.GetTeamsUseCase
import com.soccersim.presentation.ui.standings.StandingsViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class StandingsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val getTeamsUseCase = mockk<GetTeamsUseCase>()
    private val getTeamsStatisticsUseCase = mockk<GetTeamsStatisticsUseCase>()
    private val generateTeamStandingsInfoUseCase = mockk<GenerateTeamStandingsInfoUseCase>()

    private lateinit var viewModel: StandingsViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = StandingsViewModel(
            getTeamsUseCase,
            getTeamsStatisticsUseCase,
            generateTeamStandingsInfoUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchTeams should fetch teams and then fetch statistics if teams are successfully retrieved`() = runTest {
        // Given
        val teams = listOf(mockk<Team>())
        val standings = listOf(mockk<TeamStandingInfo>())
        coEvery { getTeamsUseCase() } returns Result.success(teams)
        coEvery { getTeamsStatisticsUseCase() } returns Result.success(emptyList())
        every { generateTeamStandingsInfoUseCase(teams, emptyList()) } returns standings

        val standingsObserver = mockk<Observer<List<TeamStandingInfo>>>(relaxed = true)
        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.standings.observeForever(standingsObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        // When
        viewModel.fetchTeams()

        // Then
        coVerify { getTeamsUseCase() }
        coVerify { getTeamsStatisticsUseCase() }
        verify { generateTeamStandingsInfoUseCase(teams, emptyList()) }
        verify { standingsObserver.onChanged(standings) }
        verify { loadingObserver.onChanged(true) }
        verify { loadingObserver.onChanged(false) }
    }

    @Test
    fun `fetchTeams should handle failure when getTeamsUseCase returns failure`() = runTest {
        // Given
        val exception = Exception("Test exception")
        coEvery { getTeamsUseCase() } returns Result.failure(exception)

        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isLoading.observeForever(loadingObserver)

        // When
        viewModel.fetchTeams()

        // Then
        coVerify { getTeamsUseCase() }
        verify { loadingObserver.onChanged(true) }
        verify { loadingObserver.onChanged(false) }
    }

    @Test
    fun `fetchTeams should handle failure when getTeamsStatisticsUseCase returns failure`() = runTest {
        // Given
        val teams = listOf(mockk<Team>())
        val exception = Exception("Test exception")
        coEvery { getTeamsUseCase() } returns Result.success(teams)
        coEvery { getTeamsStatisticsUseCase() } returns Result.failure(exception)

        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isLoading.observeForever(loadingObserver)

        // When
        viewModel.fetchTeams()

        // Then
        coVerify { getTeamsUseCase() }
        coVerify { getTeamsStatisticsUseCase() }
        verify { loadingObserver.onChanged(true) }
        verify { loadingObserver.onChanged(false) }
    }
}