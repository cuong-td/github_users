package star.global.gitusers.presentation.deps

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import star.global.gitusers.presentation.search.SearchViewModel

@Module
abstract class ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindUserSearchViewModel(vm: SearchViewModel): ViewModel
}