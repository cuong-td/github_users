package star.global.gitusers.usecase.deps

import star.global.gitusers.usecase.DoSomething
import star.global.gitusers.usecase.impl.DoSomethingImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UseCasesModule {
    // TODO: Binds UseCase implementation
    @Binds
    @Singleton
    abstract fun bindDoSomething(impl: DoSomethingImpl): DoSomething
}