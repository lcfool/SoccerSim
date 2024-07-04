package com.soccersim.presentation.ui.rounds

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Round
import com.soccersim.domain.usecases.matches.GenerateMatchesUseCase
import com.soccersim.domain.usecases.matches.GetMatchesUseCase
import com.soccersim.domain.usecases.matches.GroupMatchesIntoRoundsUseCase
import com.soccersim.domain.usecases.matches.SaveMatchesUseCase
import com.soccersim.domain.usecases.matches.SimulateMatchesUseCase
import com.soccersim.domain.usecases.statistics.UpdateTeamStatisticsUseCase
import com.soccersim.presentation.ui.rounds.RoundsViewModel
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
class RoundsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val getMatchesUseCase = mockk<GetMatchesUseCase>()
    private val simulateMatchUseCase = mockk<SimulateMatchesUseCase>()
    private val saveMatchesUseCase = mockk<SaveMatchesUseCase>()
    private val generateMatchesUseCase = mockk<GenerateMatchesUseCase>()
    private val groupMatchesIntoRoundsUseCase = mockk<GroupMatchesIntoRoundsUseCase>()
    private val updateTeamStatisticsUseCase = mockk<UpdateTeamStatisticsUseCase>()

    private lateinit var viewModel: RoundsViewModel
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        viewModel = RoundsViewModel(
            getMatchesUseCase,
            simulateMatchUseCase,
            saveMatchesUseCase,
            generateMatchesUseCase,
            groupMatchesIntoRoundsUseCase,
            updateTeamStatisticsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMatches should fetch matches and generate rounds if matches are not empty`() = runTest {
        // Given
        val matches = listOf(mockk<Match>())
        val rounds = listOf(mockk<Round>())
        coEvery { getMatchesUseCase() } returns Result.success(matches)
        every { groupMatchesIntoRoundsUseCase(matches) } returns rounds

        val roundsObserver = mockk<Observer<List<Round>>>(relaxed = true)
        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.rounds.observeForever(roundsObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        // When
        viewModel.fetchMatches()

        // Then
        coVerify { getMatchesUseCase() }
        verify { groupMatchesIntoRoundsUseCase(matches) }
        verify { roundsObserver.onChanged(rounds) }
        verify { loadingObserver.onChanged(true) }
        verify { loadingObserver.onChanged(false) }
    }

    @Test
    fun `fetchMatches should generate matches if matches are empty`() = runTest {
        // Given
        val matches = emptyList<Match>()
        coEvery { getMatchesUseCase() } returns Result.success(matches)
        coEvery { generateMatchesUseCase() } returns Result.success(emptyList())

        val roundsObserver = mockk<Observer<List<Round>>>(relaxed = true)
        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.rounds.observeForever(roundsObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        // When
        viewModel.fetchMatches()

        // Then
        coVerify { getMatchesUseCase() }
        coVerify { generateMatchesUseCase() }
        verify { loadingObserver.onChanged(true) }
        verify { loadingObserver.onChanged(false) }
    }

    @Test
    fun `simulateMatches should save simulated matches and update team statistics on success`() = runTest {
        // Given
        val rounds = listOf(mockk<Round>())
        val simulatedMatches = listOf(mockk<Match>())
        every { groupMatchesIntoRoundsUseCase(any()) } returns rounds
        coEvery { simulateMatchUseCase(any()) } returns Result.success(simulatedMatches)
        coEvery { saveMatchesUseCase(any()) } returns Result.success(Unit)
        coEvery { updateTeamStatisticsUseCase(any()) } returns Result.success(Unit)

        val roundsObserver = mockk<Observer<List<Round>>>(relaxed = true)
        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.rounds.observeForever(roundsObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        // When
        viewModel.simulateMatches()

        // Then
        coVerify { simulateMatchUseCase(any()) }
        coVerify { saveMatchesUseCase(simulatedMatches) }
        coVerify { updateTeamStatisticsUseCase(simulatedMatches) }
        verify { roundsObserver.onChanged(rounds) }
        verify { loadingObserver.onChanged(true) }
        verify { loadingObserver.onChanged(false) }
    }
}