package kz.zeme.converter.features

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kz.zeme.converter.core.activity.ActivityProvider
import kz.zeme.converter.core.activity.extensions.setLocale
import kz.zeme.converter.core.locale.domain.LocaleRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

open class AppGlobalActivityProvider :
    Application.ActivityLifecycleCallbacks,
    ActivityProvider,
    KoinComponent {

    private val localeRepository: LocaleRepository by inject()

    override var currentActivity: AppCompatActivity? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity as? AppCompatActivity
        activity.setLocale(localeRepository.getSelectedLocale())
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity as? AppCompatActivity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity as? AppCompatActivity
        currentActivity?.enableEdgeToEdge()
        activity.setLocale(localeRepository.getSelectedLocale())
    }

    override fun onActivityPaused(activity: Activity) = clear(activity)

    override fun onActivityStopped(activity: Activity) = clear(activity)

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = clear(activity)

    override fun onActivityDestroyed(activity: Activity) = clear(activity)

    private fun clear(activity: Activity) {
        if (currentActivity != activity) return
        currentActivity = null
    }
}
