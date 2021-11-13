package star.global.gitusers.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import star.global.gitusers.R
import star.global.gitusers.base.BaseViewModel
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.Error
import star.global.gitusers.domain.left
import star.global.gitusers.domain.right
import star.global.gitusers.domain.user.BriefUser
import star.global.gitusers.usecase.FindUsers
import star.global.gitusers.usecase.GetUserDetail
import javax.inject.Inject

class SearchViewModel
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

    private var page: Int = 0
    private var lastKeywords = ""
    private var lastResults: List<BriefUser> = emptyList()
    private var hasMoreData: Boolean = true

    private var searchJob: Job? = null
    private var processSearchingJob: Job? = null


    fun search(keywords: String, page: Int = 1) {
        if (keywords.isEmpty()) {
            searchJob?.cancel()
            searchState.setData(SearchState.InitState)
            return
        }
        if (page > 1 && searchJob?.isActive == true) return
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchState.setData(SearchState.LoadingState(page == 1))
            lastKeywords = keywords
            this@SearchViewModel.page = page
            if (page == 1) lastResults = emptyList()

            findUsers(keywords, page)
                .flowOn(dispatcher)
                .collect {
                    when (it) {
                        is Either.Left -> {
                            when (it.left.code) {
                                Error.NOT_FOUND -> searchState.setData(SearchState.ErrorState(R.string.err_not_found.left()))
                                Error.UNKNOWN -> searchState.setData(SearchState.ErrorState(R.string.err_unknown.left()))
                                Error.NETWORK -> searchState.setData(SearchState.ErrorState(R.string.err_network.left()))
                                else -> searchState.setData(SearchState.ErrorState(it.left.message.right()))
                            }
                        }
                        is Either.Right -> {
                            lastResults = lastResults + it.right.users
                            hasMoreData = it.right.total > lastResults.count()
                            if (lastResults.isEmpty())
                                searchState.setData(SearchState.ErrorState(R.string.err_not_found.left()))
                            else
                                searchState.setData(
                                    SearchState.SuccessState(
                                        it.right.total,
                                        lastResults
                                    )
                                )
                        }
                        else -> {
                            // TODO: Should add log to remote for this case
                            searchState.setData(SearchState.ErrorState(R.string.err_unknown.left()))
                        }
                    }
                }
        }
    }

    fun searchNext() {
        if (hasMoreData)
            search(lastKeywords, page + 1)
    }

    fun processSearching(keywords: String) {
        if (keywords.length < MIN_SEARCHING_LEN) return
        processSearchingJob?.cancel()
        processSearchingJob = viewModelScope.launch {
            withContext(dispatcher) {
                delay(DEBOUNCE_TIME)
                flowOf(keywords).collect { search(it) }
            }
        }
    }
}