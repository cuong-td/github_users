package star.global.gitusers.data.deps

import dagger.Binds
import dagger.Module
import star.global.gitusers.data.repository.UserRepository
import star.global.gitusers.data.repository.impl.UserRepoImpl
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepo(impl: UserRepoImpl): UserRepository
}