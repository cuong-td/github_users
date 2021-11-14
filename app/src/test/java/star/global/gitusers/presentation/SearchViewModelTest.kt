package star.global.gitusers.presentation

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import star.global.gitusers.data.mapper.toModel
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.left
import star.global.gitusers.domain.right
import star.global.gitusers.genSearchData
import star.global.gitusers.presentation.search.SearchState
import star.global.gitusers.presentation.search.SearchViewModel
import star.global.gitusers.usecase.FindUsers

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SearchViewModelTest {
    private val dispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var observerState: Observer<SearchState>

    @Mock
    private lateinit var findUsers: FindUsers

    @Captor
    lateinit var loadingCaptor: ArgumentCaptor<SearchState.LoadingState>

    @Captor
    lateinit var errCaptor: ArgumentCaptor<SearchState.ErrorState>

    @Captor
    lateinit var dataCaptor: ArgumentCaptor<SearchState.SuccessState>

    private lateinit var vm: SearchViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun postToMainThread(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true
            })
        loadingCaptor = ArgumentCaptor.forClass(SearchState.LoadingState::class.java)
        errCaptor = ArgumentCaptor.forClass(SearchState.ErrorState::class.java)
        dataCaptor = ArgumentCaptor.forClass(SearchState.SuccessState::class.java)
        vm = SearchViewModel(findUsers, dispatcher)
        vm.searchState.observeForever(observerState)
    }

    @After
    internal fun tearDown() {
        ArchTaskExecutor.getInstance().setDelegate(null)
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `search with success values`() {
        dispatcher.runBlockingTest {
            vm.search("")
            verify(observerState).onChanged(SearchState.InitState)

            val keywords = "data"
            val successResult = genSearchData().toModel()

            `when`(findUsers(keywords, 1)).thenReturn(flow {
                delay(500)
                emit(successResult.right())
            })
            vm.search(keywords)

            verify(observerState, atLeastOnce()).onChanged(loadingCaptor.capture())
            assertTrue(loadingCaptor.value.firstLoading)

            advanceUntilIdle()
            verify(observerState).onChanged(any(SearchState.SuccessState::class.java))
            verify(observerState, atLeastOnce()).onChanged(dataCaptor.capture())
            assertEquals(15, dataCaptor.value.data.count())

            `when`(findUsers(keywords, 2)).thenReturn(flow {
                delay(500)
                emit(successResult.right())
            })
            vm.searchNext()
            verify(observerState, atLeastOnce()).onChanged(loadingCaptor.capture())
            assertFalse(loadingCaptor.value.firstLoading)

            advanceUntilIdle()
            verify(observerState, atLeastOnce()).onChanged(dataCaptor.capture())
            assertEquals(30, dataCaptor.value.data.count())
        }
    }

    @Test
    fun `search with error values`() {
        dispatcher.runBlockingTest {
            dispatcher.runBlockingTest {
                val keywords = "data"
                `when`(findUsers(keywords, 1)).thenReturn(flow {
                    delay(500)
                    emit(Error.errorData(0, "Not found").left())
                })
                vm.search(keywords)
                advanceUntilIdle()
                verify(observerState, atLeastOnce()).onChanged(errCaptor.capture())
                errCaptor.value.either
                assertTrue(errCaptor.value.either is Either.Right)
                assertEquals((errCaptor.value.either as Either.Right).right, "Not found")
            }
        }
    }
}