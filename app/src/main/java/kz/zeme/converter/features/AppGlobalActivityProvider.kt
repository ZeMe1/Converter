package kz.zeme.converter.features

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kz.zeme.converter.core.activity.ActivityProvider

open class AppGlobalActivityProvider :
    Application.ActivityLifecycleCallbacks,
    ActivityProvider {

    override var currentActivity: AppCompatActivity? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity as? AppCompatActivity
    }

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity as? AppCompatActivity
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity as? AppCompatActivity
        currentActivity?.enableEdgeToEdge()
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
