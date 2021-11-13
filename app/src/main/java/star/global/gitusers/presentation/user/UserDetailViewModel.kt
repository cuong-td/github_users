package star.global.gitusers.presentation.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import star.global.gitusers.base.BaseViewModel
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.user.User
import star.global.gitusers.usecase.GetUserDetail
import javax.inject.Inject

class UserDetailViewModel
@Inject
constructor(
    private val getUser: GetUserDetail,
    private val dispatcher: CoroutineDispatcher
) : BaseViewModel() {
    val errorMessage: LiveData<String> = MutableLiveData()
    val user: LiveData<User> = MutableLiveData()

    fun loadUserDetail(username: String) {
        viewModelScope.launch {
            getUser(username)
                .flowOn(dispatcher)
                .collect {
                    when (it) {
                        is Either.Left -> {
                            errorMessage.setData(it.left.message)
                        }
                        is Either.Right -> {
                            user.setData(it.right)
                        }
                        else -> {
                        }
                    }
                }
        }
    }
}