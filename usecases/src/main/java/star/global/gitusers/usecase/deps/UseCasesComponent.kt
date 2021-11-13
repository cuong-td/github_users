package star.global.gitusers.usecase.deps

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UseCasesModule::class])
interface UseCasesComponent : UseCasesExposeProvider {
    @Component.Builder
    interface Builder {
        //        @BindsInstance
//        fun bindRepo(repo: Repository): Builder
        fun build(): UseCasesComponent
    }
}