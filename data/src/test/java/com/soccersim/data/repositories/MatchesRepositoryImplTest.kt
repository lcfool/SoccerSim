package com.soccersim.data.repositories

import com.soccersim.data.datasources.matches.MatchesDataSource
import com.soccersim.data.models.MatchEntity
import com.soccersim.data.models.TeamEntity
import com.soccersim.domain.models.Match
import com.soccersim.domain.models.Team
import com.soccersim.domain.repository.MatchesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MatchesRepositoryImplTest {

    private lateinit var localDataSource: MatchesDataSource
    private lateinit var remoteDataSource: MatchesDataSource
    private lateinit var repository: MatchesRepository

    @Before
    fun setUp() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        repository = MatchesRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `getMatches should return a list of matches successfully`() = runTest {
        // Given
        val matchEntities = listOf(
            MatchEntity(1, TeamEntity(1, "Team A", 5), TeamEntity(2, "Team B", 4)),
            MatchEntity(2, TeamEntity(3, "Team C", 3), TeamEntity(4, "Team D", 2))
        )
        coEvery { localDataSource.getMatches() } returns Result.success(matchEntities)

        // When
        val result = repository.getMatches()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getMatches should return a failure result when localDataSource fails`() = runTest {
        // Given
        val exception = Exception("Test Exception")
        coEvery { localDataSource.getMatches() } returns Result.failure(exception)

        // When
        val result = repository.getMatches()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }

    @Test
    fun `saveMatches should save matches successfully`() = runTest {
        // Given
        val matches = listOf(
            Match(1, Team(1, "Team A", 5), Team(2, "Team B", 4)),
            Match(2, Team(3, "Team C", 3), Team(4, "Team D", 2))
        )
        coEvery { localDataSource.saveMatches(any()) } returns Result.success(Unit)

        // When
        val result = repository.saveMatches(matches)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `saveMatches should return a failure result when localDataSource fails`() = runTest {
        // Given
        val matches = listOf(
            Match(1, Team(1, "Team A", 5), Team(2, "Team B", 4)),
            Match(2, Team(3, "Team C", 3), Team(4, "Team D", 2))
        )
        val exception = Exception("Test Exception")
        coEvery { localDataSource.saveMatches(any()) } returns Result.failure(exception)

        // When
        val result = repository.saveMatches(matches)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Test Exception", result.exceptionOrNull()?.message)
    }
}