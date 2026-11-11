package kz.zeme.converter.features.navigation.deeplink

import android.content.Intent
import android.os.Bundle
import kz.zeme.converter.core.navigation.BaseNavigation
import kz.zeme.converter.core.navigation.Navigator
import kz.zeme.converter.core.navigation.extensions.getRedirectOrNull

class AppGlobalRedirectParser : AppRedirectParser {

    override fun getRedirectOrNull(intent: Intent?): Navigator.Navigation? {
        intent ?: return null

        intent.getRedirectOrNull()?.let {
            if (it is BaseNavigation.ShowMain) return it
            return BaseNavigation.ShowMain(restart = false, redirect = it, withSplash = false)
        }

        return null
    }

    override fun getRedirectOrNull(bundle: Bundle?): Navigator.Navigation? =
        bundle?.getRedirectOrNull()
}
