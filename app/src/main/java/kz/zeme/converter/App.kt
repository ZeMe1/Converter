package kz.zeme.converter

import android.app.Application
import kz.zeme.converter.di.KoinController
import kz.zeme.converter.di.KoinModules
import org.koin.android.ext.android.get

open class App : Application() {

    open val koinModules: KoinModules = KoinModules()

    override fun onCreate() {
        super.onCreate()

        KoinController.onStart(
            application = this,
            modules = koinModules.getModules(),
        )

        registerActivityLifecycleCallbacks(get<ActivityLifecycleCallbacks>())
    }
}
