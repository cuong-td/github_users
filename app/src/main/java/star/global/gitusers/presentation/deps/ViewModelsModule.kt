package star.global.gitusers.presentation.deps

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import star.global.gitusers.presentation.search.SearchViewModel
import star.global.gitusers.presentation.user.UserDetailViewModel

@Module
abstract class ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindUserSearchViewModel(vm: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailViewModel::class)
    abstract fun bindUserDetailViewModel(vm: UserDetailViewModel): ViewModel
}