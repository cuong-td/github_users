package star.global.gitusers.presentation.search

import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import star.global.gitusers.R
import star.global.gitusers.domain.Either
import star.global.gitusers.domain.user.BriefUser

class SearchBindingModel {
    val errorVisibility = ObservableInt(View.VISIBLE)
    val errorMessage = ObservableField("")
    val loadingVisibility = ObservableInt(View.GONE)
    val dataVisibility = ObservableInt(View.GONE)

    val users = ObservableField<List<BriefUser>>(emptyList())
    val totalUsers = ObservableInt(0)
    val loadedUsers = ObservableInt(0)

    fun bind(context: Context, state: SearchState) {
        when (state) {
            SearchState.InitState -> {
                loadingVisibility.set(View.GONE)
                errorVisibility.set(View.VISIBLE)
                dataVisibility.set(View.GONE)
                errorMessage.set(context.getString(R.string.no_data))
            }
            is SearchState.LoadingState -> {
                loadingVisibility.set(View.VISIBLE)
                errorVisibility.set(View.GONE)
                dataVisibility.set(if (state.firstLoading) View.GONE else View.VISIBLE)
            }
            is SearchState.ErrorState -> {
                loadingVisibility.set(View.GONE)
                errorVisibility.set(View.VISIBLE)
                dataVisibility.set(View.GONE)
                errorMessage.set(
                    when (state.either) {
                        is Either.Left -> context.getString(state.either.left)
                        is Either.Right -> state.either.right
                        else -> context.getString(R.string.err_unknown)
                    }
                )
            }
            is SearchState.SuccessState -> {
                loadingVisibility.set(View.GONE)
                errorVisibility.set(View.GONE)
                dataVisibility.set(View.VISIBLE)
                users.set(state.data)
                totalUsers.set(state.total)
                loadedUsers.set(state.data.count())
            }
        }
    }
}