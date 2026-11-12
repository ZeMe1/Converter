package kz.zeme.converter.di

import android.app.Application
import kz.zeme.converter.core.env.props.KoinProperties
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

object KoinController {

    fun onStart(
        application: Application,
        modules: List<Module>,
    ) {
        startKoin {
            allowOverride(true)
            androidContext(application)
            properties(KoinProperties.getKoinProperties())
            modules(modules = modules)
        }
    }
}
