package star.global.gitusers.repo

import junit.framework.Assert.assertTrue
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import star.global.gitusers.data.remote.RemoteSource
import star.global.gitusers.data.repository.impl.UserRepoImpl
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.genSearchData
import star.global.gitusers.genUserDetail

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class RepoTest {

    @Mock
    private lateinit var remoteSource: RemoteSource

    private lateinit var repo: UserRepoImpl

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repo = UserRepoImpl(remoteSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `check success search data from remote`() {
        val query = "data"
        dispatcher.runBlockingTest {
            `when`(remoteSource.fetchUsers(query, 1)).thenReturn(genSearchData())
            val either = repo.fetchUsers(query).first()
            assertTrue(either is Either.Right)
            with((either as Either.Right).right) {
                assertEquals(10000, total)
                assertEquals(15, users.count())
                assertEquals(
                    "https://images.unsplash.com/photo-1453728013993-6d66e9c9123a?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dmlld3xlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60",
                    users.first().avatarUrl
                )
            }
        }
    }

    @Test
    fun `check success get detail from remote`() {
        val query = "data"
        dispatcher.runBlockingTest {
            `when`(remoteSource.fetchUserDetail(query)).thenReturn(genUserDetail(query))
            val either = repo.fetchUser(query).first()
            assertTrue(either is Either.Right)
            with((either as Either.Right).right) {
                assertTrue(username.startsWith(query))
                TestCase.assertEquals("N/A", name)
                TestCase.assertEquals("", avatarUrl)
                TestCase.assertEquals("N/A", company)
                TestCase.assertEquals("N/A", location)
                TestCase.assertEquals("N/A", email)
                TestCase.assertEquals("N/A", bio)
                TestCase.assertEquals(0, followers)
                TestCase.assertEquals(0, following)
                TestCase.assertEquals(0, repos)
                TestCase.assertEquals(0, gists)
            }
        }
    }

    @Test
    fun `check unknown error on searching data from remote`() {
        dispatcher.runBlockingTest {
            `when`(remoteSource.fetchUsers("", 1)).thenThrow(OutOfMemoryError())
            val either = repo.fetchUsers("").first()
            assertTrue(either is Either.Left)
            with(either as Either.Left) {
                assertEquals(Error.UNKNOWN, left.code)
            }
        }
    }
}