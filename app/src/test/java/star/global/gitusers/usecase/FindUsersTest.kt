package star.global.gitusers.usecase

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import star.global.gitusers.data.mapper.toModel
import star.global.gitusers.data.repository.UserRepository
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.left
import star.global.gitusers.domain.right
import star.global.gitusers.genSearchData

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class FindUsersTest {
    @Mock
    private lateinit var repo: UserRepository

    @InjectMocks
    private lateinit var findUsers: FindUsers

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `check success search data from repo`() {
        dispatcher.runBlockingTest {
            val keyword = "data"
            `when`(repo.fetchUsers("$keyword in:name")).thenReturn(flow {
                emit(genSearchData().toModel().right())
            })
            val either = findUsers(keyword, 1).first()
            assertTrue(either is Either.Right)
            with((either as Either.Right).right) {
                Assert.assertEquals(10000, total)
                Assert.assertEquals(15, users.count())
                Assert.assertEquals(
                    "https://images.unsplash.com/photo-1453728013993-6d66e9c9123a?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dmlld3xlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=60",
                    users.first().avatarUrl
                )
            }
        }
    }

    @Test
    fun `check unknown error on searching data from repo`() {
        dispatcher.runBlockingTest {
            `when`(repo.fetchUsers(" in:username")).thenReturn(flow {
                emit(Error.unknown().left())
            })
            val either = findUsers("", 1).first()
            assertTrue(either is Either.Left)
            with(either as Either.Left) {
                assertEquals(Error.UNKNOWN, left.code)
            }
        }
    }
}