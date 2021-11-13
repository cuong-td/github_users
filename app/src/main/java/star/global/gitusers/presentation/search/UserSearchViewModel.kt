package star.global.gitusers.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import star.global.gitusers.R
import star.global.gitusers.base.BaseViewModel
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.left
import star.global.gitusers.domain.right
import star.global.gitusers.usecase.FindUsers
import star.global.gitusers.usecase.GetUserDetail
import javax.inject.Inject

class UserSearchViewModel
@Inject
constructor(
    private val findUsers: FindUsers,
    private val getUser: GetUserDetail,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel() {
    companion object {
        const val MIN_SEARCHING_LEN = 3
        const val DEBOUNCE_TIME = 700L
    }

    val searchState: LiveData<SearchState> = MutableLiveData()

    private var searchJob: Job? = null
    private var processSearchingJob: Job? = null


    fun search(keywords: String, page: Int = 1) {
        if (keywords.isEmpty()) return
        println("TdcTest: Searching $keywords ...")
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchState.setData(SearchState.LoadingState)
            withContext(dispatcher) {
                findUsers(keywords, page).collect {
                    when (it) {
                        is Either.Left -> {
                            when (it.left.code) {
                                Error.NOT_FOUND -> searchState.postData(SearchState.ErrorState(R.string.err_not_found.left()))
                                Error.UNKNOWN -> searchState.postData(SearchState.ErrorState(R.string.err_unknown.left()))
                                Error.NETWORK -> searchState.postData(SearchState.ErrorState(R.string.err_network.left()))
                                else -> searchState.postData(SearchState.ErrorState(it.left.message.right()))
                            }
                        }
                        is Either.Right -> {
                            searchState.postData(SearchState.SuccessState(it.right))
                        }
                        else -> {
                            // TODO: Should add log to remote for this case
                            searchState.postData(SearchState.ErrorState(R.string.err_unknown.left()))
                        }
                    }
                }
            }
        }
    }

    fun processSearching(keywords: String) {
        if (keywords.length < MIN_SEARCHING_LEN) return
        processSearchingJob?.cancel()
        processSearchingJob = viewModelScope.launch {
            println("TdcTest: Processing $keywords ...")
            withContext(dispatcher) {
                delay(DEBOUNCE_TIME)
                flowOf(keywords).collect { search(it) }
            }
        }
    }
}