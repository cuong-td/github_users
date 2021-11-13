package star.global.gitusers.usecase.deps

import dagger.Binds
import dagger.Module
import star.global.gitusers.usecase.FindUsers
import star.global.gitusers.usecase.GetUserDetail
import star.global.gitusers.usecase.impl.FindUsersImpl
import star.global.gitusers.usecase.impl.GetUserImpl
import javax.inject.Singleton

@Module
abstract class UseCasesModule {
    @Binds
    @Singleton
    abstract fun bindFindUsers(impl: FindUsersImpl): FindUsers

    @Binds
    @Singleton
    abstract fun bindGetUser(impl: GetUserImpl): GetUserDetail
}