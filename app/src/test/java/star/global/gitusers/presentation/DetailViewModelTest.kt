package star.global.gitusers.presentation

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import star.global.gitusers.data.mapper.toUser
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.left
import star.global.gitusers.domain.right
import star.global.gitusers.domain.user.User
import star.global.gitusers.genUserDetail
import star.global.gitusers.presentation.user.UserDetailViewModel
import star.global.gitusers.usecase.GetUserDetail

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class DetailViewModelTest {
    private val dispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var observerMessage: Observer<String>

    @Mock
    private lateinit var observerUser: Observer<User>

    @Mock
    private lateinit var getUser: GetUserDetail

    @Captor
    lateinit var msgCaptor: ArgumentCaptor<String>

    private lateinit var vm: UserDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun postToMainThread(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true
            })
        msgCaptor = ArgumentCaptor.forClass(String::class.java)
        vm = UserDetailViewModel(getUser, dispatcher)
        vm.user.observeForever(observerUser)
        vm.errorMessage.observeForever(observerMessage)
    }

    @After
    internal fun tearDown() {
        ArchTaskExecutor.getInstance().setDelegate(null)
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `load user detail success`() {
        dispatcher.runBlockingTest {
            val username = "data"
            val result = genUserDetail(username).toUser()

            `when`(getUser(username)).thenReturn(flow {
                emit(result.right())
            })
            vm.loadUserDetail(username)

            advanceUntilIdle()
            verify(observerUser).onChanged(any(User::class.java))
        }
    }

    @Test
    fun `load user error`() {
        dispatcher.runBlockingTest {
            `when`(getUser("")).thenReturn(flow {
                emit(Error.errorData(Error.NOT_FOUND, "Not found").left())
            })
            vm.loadUserDetail("")
            advanceUntilIdle()
            verify(observerMessage).onChanged(msgCaptor.capture())
            assertEquals("Not found", msgCaptor.value)
        }
    }
}