package star.global.gitusers.deps

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import star.global.gitusers.presentation.deps.ViewModelFactory
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindViewModelFactory(vmFactory: ViewModelFactory): ViewModelProvider.Factory
}