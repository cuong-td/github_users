package star.global.gitusers.usecase.deps

import dagger.BindsInstance
import dagger.Component
import star.global.gitusers.data.repository.UserRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [UseCasesModule::class])
interface UseCasesComponent : UseCasesExposeProvider {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindUserRepository(repo: UserRepository): Builder
        fun build(): UseCasesComponent
    }
}