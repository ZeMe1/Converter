package kz.zeme.converter.core.navigation.extensions

import android.content.Intent
import android.os.Bundle
import kz.zeme.converter.core.navigation.Navigator

private const val REDIRECT_KEY = "REDIRECT_KEY"

fun Intent.putRedirect(redirect: Navigator.Navigation?): Intent = apply {
    putExtra(REDIRECT_KEY, redirect)
}

fun Bundle.putRedirect(redirect: Navigator.Navigation?): Bundle = apply {
    putParcelable(REDIRECT_KEY, redirect)
}

fun Intent.getRedirectOrNull(): Navigator.Navigation? =
    extras?.getRedirectOrNull()

@Suppress("DEPRECATION")
fun Bundle.getRedirectOrNull(): Navigator.Navigation? =
    getParcelable(REDIRECT_KEY)
