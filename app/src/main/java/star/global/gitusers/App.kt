package star.global.gitusers

import androidx.multidex.MultiDexApplication
import com.nab.weatherforecast.DiDelegate
import star.global.gitusers.deps.ActivityInjectionProvider
import star.global.gitusers.deps.InjectorProvider

class App : MultiDexApplication(), InjectorProvider {
    lateinit var diDelegate: DiDelegate
    override fun onCreate() {
        super.onCreate()
        diDelegate = DiDelegate(this)
    }

    override fun activityInjector(): ActivityInjectionProvider = diDelegate.appComponent
}