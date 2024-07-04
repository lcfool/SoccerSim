import com.soccersim.data.datasources.matches.LocalMatchesDataSource
import com.soccersim.data.models.MatchEntity
import com.soccersim.data.models.TeamEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LocalMatchesDataSourceTest {

    private lateinit var localMatchesDataSource: LocalMatchesDataSource

    @Before
    fun setUp() {
        localMatchesDataSource = LocalMatchesDataSource()
    }

    @Test
    fun `getMatches should return the current list of matches`() = runTest {
        // Given
        val teamA = TeamEntity(1, "Team A", 5)
        val teamB = TeamEntity(2, "Team B", 6)
        val teamC = TeamEntity(3, "Team C", 7)
        val teamD = TeamEntity(4, "Team D", 8)

        val expectedMatches = listOf(
            MatchEntity(1, teamA, teamB),
            MatchEntity(2, teamC, teamD)
        )
        localMatchesDataSource.saveMatches(expectedMatches)

        // When
        val result = localMatchesDataSource.getMatches()

        // Then
        assertEquals(Result.success(expectedMatches), result)
    }

    @Test
    fun `getMatches should return empty list when no matches are saved`() = runTest {
        // When
        val result = localMatchesDataSource.getMatches()

        // Then
        assertEquals(Result.success(emptyList<MatchEntity>()), result)
    }

    @Test
    fun `saveMatches should update the matches list`() = runTest {
        // Given
        val teamA = TeamEntity(1, "Team A", 5)
        val teamB = TeamEntity(2, "Team B", 6)
        val teamC = TeamEntity(3, "Team C", 7)
        val teamD = TeamEntity(4, "Team D", 8)
        val teamE = TeamEntity(5, "Team E", 9)
        val teamF = TeamEntity(6, "Team F", 10)

        val initialMatches = listOf(
            MatchEntity(1, teamA, teamB)
        )
        localMatchesDataSource.saveMatches(initialMatches)

        val newMatches = listOf(
            MatchEntity(2, teamC, teamD),
            MatchEntity(3, teamE, teamF)
        )

        // When
        val saveResult = localMatchesDataSource.saveMatches(newMatches)
        val getResult = localMatchesDataSource.getMatches()

        // Then
        assertEquals(Result.success(Unit), saveResult)
        assertEquals(Result.success(newMatches), getResult)
    }

    @Test
    fun `saveMatches with empty list should clear the matches list`() = runTest {
        // Given
        val initialMatches = listOf(
            MatchEntity(id = 1, teamA = TeamEntity(1, "Team A", 80), teamB = TeamEntity(2, "Team B", 75), goalsTeamA = 2, goalsTeamB = 1)
        )
        localMatchesDataSource.saveMatches(initialMatches)

        // When
        val saveResult = localMatchesDataSource.saveMatches(emptyList())
        val getResult = localMatchesDataSource.getMatches()

        // Then
        assertEquals(Result.success(Unit), saveResult)
        assertEquals(Result.success(emptyList<MatchEntity>()), getResult)
    }

    @Test
    fun `concurrent access to saveMatches and getMatches should be thread-safe`() = runTest {
        // Given
        val matchesList1 = listOf(
            MatchEntity(id = 1, teamA = TeamEntity(1, "Team A", 80), teamB = TeamEntity(2, "Team B", 75), goalsTeamA = 2, goalsTeamB = 1)
        )
        val matchesList2 = listOf(
            MatchEntity(id = 2, teamA = TeamEntity(3, "Team C", 85), teamB = TeamEntity(4, "Team D", 70), goalsTeamA = 3, goalsTeamB = 0)
        )

        // When
        val job1 = launch { localMatchesDataSource.saveMatches(matchesList1) }
        val job2 = launch { localMatchesDataSource.saveMatches(matchesList2) }
        joinAll(job1, job2)
        val result = localMatchesDataSource.getMatches()

        // Then
        // Ensure that one of the lists is saved and returned
        assert(result.isSuccess)
        assert(result.getOrThrow() in listOf(matchesList1, matchesList2))
    }
}