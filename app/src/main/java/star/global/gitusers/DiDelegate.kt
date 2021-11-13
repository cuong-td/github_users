package star.global.gitusers

import star.global.gitusers.data.deps.DaggerDataComponent
import star.global.gitusers.data.deps.DataComponent
import star.global.gitusers.data.deps.DataConfigs
import star.global.gitusers.deps.AppComponent
import star.global.gitusers.deps.DaggerAppComponent
import star.global.gitusers.usecase.deps.DaggerUseCasesComponent
import star.global.gitusers.usecase.deps.UseCasesComponent

open class DiDelegate() {

    private val dataComponent: DataComponent by lazy {
        DaggerDataComponent.builder()
            .bindConfigurations(DataConfigs(BuildConfig.BASE_URL))
            .build()
    }

    private val useCasesComponent: UseCasesComponent by lazy {
        DaggerUseCasesComponent.builder()
            .bindUserRepository(dataComponent.userRepo)
            .build()
    }
    open val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .bindUseCaseFindUsers(useCasesComponent.findUsers)
            .bindUseCaseGetUser(useCasesComponent.getUser)
            .build()
    }

}