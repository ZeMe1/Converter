package kz.zeme.converter.splash

import kotlinx.parcelize.Parcelize
import kz.zeme.converter.core.navigation.Navigator

@Parcelize
sealed class SplashExternalNavigation : Navigator.Navigation {

    data class ShowNextScreen(
        val redirect: Navigator.Navigation? = null,
    ) : SplashExternalNavigation()
}
