package star.global.gitusers.deps

import dagger.BindsInstance
import dagger.Component
import star.global.gitusers.presentation.deps.ViewModelsModule
import star.global.gitusers.usecase.FindUsers
import star.global.gitusers.usecase.GetUserDetail
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ViewModelsModule::class,
        AppModule::class,
        CommonModule::class
    ]
)
interface AppComponent : ActivityInjectionProvider {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindUseCaseFindUsers(findUsers: FindUsers): Builder

        @BindsInstance
        fun bindUseCaseGetUser(getUserDetail: GetUserDetail): Builder

        fun build(): AppComponent
    }
}