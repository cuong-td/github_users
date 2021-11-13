package star.global.gitusers.data.deps

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, RepositoryModule::class])
interface DataComponent : DataExposeApiProvider {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun bindConfigurations(configs: DataConfigs): Builder

        fun build(): DataComponent
    }
}