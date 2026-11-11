package kz.zeme.converter.features.navigation

import kz.zeme.converter.core.activity.ActivityProvider
import kz.zeme.converter.core.activity.extensions.makeToast
import kz.zeme.converter.core.navigation.BaseNavigation
import kz.zeme.converter.core.navigation.ExternalModulesNavigator
import kz.zeme.converter.core.navigation.Navigator

class AppGlobalNavigator(
    private val activityProvider: ActivityProvider,
) : ExternalModulesNavigator {

    override fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack -> goBack()
        is BaseNavigation.Close -> close()
        is BaseNavigation.ShowToast -> showToast(navigation)
        is BaseNavigation.ShowMain -> showMain(navigation)
        else -> Unit
    }

    private fun goBack() {
        activityProvider.currentActivity?.onBackPressedDispatcher?.onBackPressed()
    }

    private fun close() {
        activityProvider.currentActivity?.finish()
    }

    private fun showToast(navigation: BaseNavigation.ShowToast) {
        val activity = activityProvider.currentActivity ?: return
        when (navigation) {
            is BaseNavigation.ShowToast.Message -> activity.makeToast(navigation.msg)
            is BaseNavigation.ShowToast.MessageErr -> activity.makeToast(navigation.error)
            is BaseNavigation.ShowToast.MessageRes -> activity.makeToast(navigation.msgRes)
        }
    }

    private fun showMain(navigation: BaseNavigation.ShowMain) {
        activityProvider.currentActivity ?: return
        if (navigation.withSplash) return

        navigation.redirect?.let(::navigate)
    }
}
