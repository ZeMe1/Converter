package kz.zeme.converter.features.navigation.deeplink

import android.content.Intent
import android.os.Bundle
import kz.zeme.converter.core.navigation.Navigator

interface AppRedirectParser {
    fun getRedirectOrNull(intent: Intent?): Navigator.Navigation?
    fun getRedirectOrNull(bundle: Bundle?): Navigator.Navigation?
}
