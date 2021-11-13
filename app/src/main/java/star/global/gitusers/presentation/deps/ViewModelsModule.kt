package star.global.gitusers.presentation.deps

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import star.global.gitusers.presentation.search.UserSearchViewModel

@Module
abstract class ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserSearchViewModel::class)
    abstract fun bindUserSearchViewModel(vm: UserSearchViewModel): ViewModel
}