package star.global.gitusers.presentation.search

import star.global.gitusers.domain.Either
import star.global.gitusers.domain.user.BriefUser

sealed class SearchState {
    object LoadingState : SearchState()
    object InitState : SearchState()
    data class ErrorState(val either: Either<Int, String>) : SearchState()
    data class SuccessState(val data: List<BriefUser>) : SearchState()
}