package kz.zeme.converter.features.navigation

import android.content.Intent
import androidx.fragment.app.commit
import kz.zeme.converter.MainActivity
import kz.zeme.converter.R
import kz.zeme.converter.core.activity.ActivityProvider
import kz.zeme.converter.core.activity.extensions.makeToast
import kz.zeme.converter.core.fragment.extensions.isShown
import kz.zeme.converter.core.locale.view.presentation.navigation.LanguagePickerNavigation
import kz.zeme.converter.core.locale.view.presentation.navigation.LanguagePickerRouter
import kz.zeme.converter.core.navigation.BaseNavigation
import kz.zeme.converter.core.navigation.ExternalModulesNavigator
import kz.zeme.converter.core.navigation.Navigator
import kz.zeme.converter.core.navigation.extensions.putRedirect
import kz.zeme.converter.splash.SplashExternalNavigation
import kz.zeme.converter.splash.SplashFragment

class AppGlobalNavigator(
    private val activityProvider: ActivityProvider,
    private val languagePickerRouter: LanguagePickerRouter,
) : ExternalModulesNavigator {

    override fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack -> goBack()
        is BaseNavigation.Close -> close()
        is BaseNavigation.ShowToast -> showToast(navigation)
        is BaseNavigation.ShowMain -> showMain(navigation)
        is SplashExternalNavigation.ShowNextScreen -> showSplashNextScreen(navigation)
        is LanguagePickerNavigation.OpenLanguagePicker -> openLanguagePicker()
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
        val activity = activityProvider.currentActivity ?: return

        if (navigation.restart) {
            val intent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putRedirect(navigation.redirect)
            }
            activity.finish()
            activity.startActivity(intent)
            return
        }

        val fm = activity.supportFragmentManager

        if (navigation.withSplash) {
            val hasSplash = fm.fragments
                .filterIsInstance<SplashFragment>()
                .isNotEmpty()

            if (hasSplash) return

            fm.commit {
                setReorderingAllowed(true)
                replace(R.id.fragment_container_view, SplashFragment())
            }
            return
        }

        fm.fragments
            .filterIsInstance<SplashFragment>()
            .forEach { splashFragment ->
                fm.commit {
                    setReorderingAllowed(true)
                    remove(splashFragment)
                }
            }

        navigation.redirect?.let(::navigate)
    }

    private fun showSplashNextScreen(navigation: SplashExternalNavigation.ShowNextScreen) {
        val redirect = (navigation.redirect as? BaseNavigation.ShowMain)?.redirect ?: navigation.redirect
        showMain(
            BaseNavigation.ShowMain(
                restart = false,
                redirect = redirect,
                withSplash = false,
            )
        )
    }

    private fun openLanguagePicker() {
        val fm = activityProvider.currentActivity?.supportFragmentManager ?: return
        val tag = LanguagePickerRouter.TAG_LANGUAGE_PICKER

        if (fm.isShown(tag)) return

        languagePickerRouter
            .getLanguagePickerFragment()
            .show(fm, tag)
    }
}
