package star.global.gitusers.presentation.search

import kotlinx.coroutines.CoroutineDispatcher
import star.global.gitusers.base.BaseViewModel
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
}