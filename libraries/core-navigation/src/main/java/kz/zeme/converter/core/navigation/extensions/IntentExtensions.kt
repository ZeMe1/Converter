package kz.zeme.converter.core.navigation.extensions

import android.content.Intent
import android.os.Build
import android.os.Parcelable

private const val INTENT_FALLBACK_URL_KEY = "INTENT_FALLBACK_URL_KEY"

inline fun <reified T : Parcelable> Intent.getTypedParcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Intent.getTypedParcelableArrayList(key: String): ArrayList<T>? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArrayListExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
}

fun Intent.getFallbackUrlOrNull(): String? = getStringExtra(INTENT_FALLBACK_URL_KEY)

fun Intent.setFallbackUrl(url: String) = apply {
    putExtra(INTENT_FALLBACK_URL_KEY, url)
}
