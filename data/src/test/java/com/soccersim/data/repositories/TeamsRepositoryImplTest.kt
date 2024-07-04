package com.soccersim.data.repositories

import com.soccersim.data.datasources.teams.TeamsDataSource
import com.soccersim.data.models.TeamEntity
import com.soccersim.domain.repository.TeamsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class TeamsRepositoryImplTest {

    private lateinit var localDataSource: TeamsDataSource
    private lateinit var remoteDataSource: TeamsDataSource
    private lateinit var repository: TeamsRepository

    @Before
    fun setUp() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        repository = TeamsRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `getTeams should return a list of teams successfully`() = runTest {
        // Given
        val teamEntities = listOf(
            TeamEntity(1, "Team A", 5),
            TeamEntity(2, "Team B", 4)
        )
        coEvery { localDataSource.getTeams() } returns Result.success(teamEntities)

        // When
        val result = repository.getTeams()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
        assertEquals("Team A", result.getOrNull()?.get(0)?.name)
        assertEquals("Team B", result.getOrNull()?.get(1)?.name)
    }

    @Test
    fun `getTeams should return a failure result when localDataSource fails`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        coEvery { localDataSource.getTeams() } returns Result.failure(exception)

        // When
        val result = repository.getTeams()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }
}