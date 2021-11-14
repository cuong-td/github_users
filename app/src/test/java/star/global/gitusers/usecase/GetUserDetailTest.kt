package star.global.gitusers.usecase

import junit.framework.Assert.assertTrue
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import star.global.gitusers.data.mapper.toUser
import star.global.gitusers.data.repository.UserRepository
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.right
import star.global.gitusers.genUserDetail

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class GetUserDetailTest {
    @Mock
    private lateinit var repo: UserRepository

    @InjectMocks
    private lateinit var getUser: GetUserDetail

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
    fun `check success get detail data from repo`() {
        dispatcher.runBlockingTest {
            val keyword = "data"
            `when`(repo.fetchUser(keyword)).thenReturn(flow {
                emit(genUserDetail(keyword).toUser().right())
            })
            val either = getUser(keyword).first()
            assertTrue(either is Either.Right)
            with((either as Either.Right).right) {
                assertTrue(username.startsWith(keyword))
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
}