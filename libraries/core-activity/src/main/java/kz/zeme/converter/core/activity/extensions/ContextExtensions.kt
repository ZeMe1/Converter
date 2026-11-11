package kz.zeme.converter.core.activity.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import kz.zeme.converter.core.res.R
import kz.zeme.converter.core.activity.extensions.HapticLevel.Companion.CONFIRM_PATTERN
import kz.zeme.converter.core.activity.extensions.HapticLevel.Companion.NO_REPEAT
import kz.zeme.converter.core.activity.extensions.HapticLevel.Companion.REJECT_PATTERN
import kz.zeme.converter.core.error.BaseError
import kz.zeme.converter.core.error.ConnectionError
import kz.zeme.converter.core.error.UnauthorizedError
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

fun Context.makeToast(
    error: BaseError,
    duration: Int = Toast.LENGTH_LONG,
    onUnauthorized: () -> Unit = { },
) {
    when (error) {
        is UnauthorizedError -> onUnauthorized()
        is ConnectionError -> makeToast(
            R.string.shared_network_error,
            duration
        )

        else -> {
            if (error.underlying is CancellationException) return

            val message = error.message
                .takeUnless { it.isNullOrBlank() }
                ?: getString(R.string.shared_unknown_error)

            makeToast(message, duration)
        }
    }
}

@JvmOverloads
fun Context.makeToast(
    @StringRes messageRes: Int,
    duration: Int = Toast.LENGTH_LONG
) {
    makeToast(getString(messageRes), duration)
}

@JvmOverloads
fun Context.makeToast(
    message: String,
    duration: Int = Toast.LENGTH_LONG
) {
    Toast.makeText(this, message, duration).show()
}

fun Context.hideKeyboard(view: View) {
    val imm = getInputMethodManagerOrNull() ?: return
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showKeyboard() {
    val imm = getInputMethodManagerOrNull() ?: return
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Context.getInputMethodManagerOrNull() =
    getSystemService(Context.INPUT_METHOD_SERVICE)
        as? InputMethodManager

fun Context.toPx(dp: Number): Int {
    val scale = resources.displayMetrics.density
    return (dp.toFloat() * scale + 0.5f).toInt()
}

fun Context.dimens(@DimenRes dimenRes: Int): Int {
    return resources.getDimension(dimenRes).toInt()
}

fun Context.bitmap(
    @DrawableRes res: Int,
    widthPx: Int? = null,
    heightPx: Int? = null
): Bitmap {
    val drawable = drawable(res)

    if (widthPx != null && heightPx != null)
        return drawable.toBitmap(widthPx, heightPx)

    return drawable.toBitmap()
}

fun Context.drawable(
    @DrawableRes res: Int,
) = ContextCompat.getDrawable(
    this,
    res
)!!

fun Context.vibrate(
    level: HapticLevel = HapticLevel.Warning,
    timings: LongArray? = null,
    amplitudes: IntArray? = null,
) {
    val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java)
        ?: return

    when (level) {
        HapticLevel.Failure,
        HapticLevel.Warning ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(REJECT_PATTERN, NO_REPEAT))
            } else {
                vibrator.vibrate(REJECT_PATTERN, NO_REPEAT)
            }

        HapticLevel.Selection,
        HapticLevel.Success ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(CONFIRM_PATTERN, NO_REPEAT))
            } else {
                vibrator.vibrate(CONFIRM_PATTERN, NO_REPEAT)
            }

        HapticLevel.Custom ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (timings == null || amplitudes == null) return

                vibrator.vibrate(
                    VibrationEffect.createWaveform(
                        timings,
                        amplitudes,
                        NO_REPEAT,
                    )
                )
            } else {
                timings?.let { vibrator.vibrate(it, NO_REPEAT) }
            }
    }
}

enum class HapticLevel {
    Failure,
    Warning,
    Selection,
    Success,
    Custom;

    companion object {
        val CONFIRM_PATTERN = longArrayOf(0, 50)
        val REJECT_PATTERN = longArrayOf(0, 50, 100, 50)
        const val NO_REPEAT = -1

        fun find(name: String?): HapticLevel? = entries.find {
            it.name.equals(name, ignoreCase = true)
        }
    }
}

fun Context.copyToClipBoard(
    text: String,
    alertMessage: String = getString(R.string.shared_copied)
) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard?.setPrimaryClip(clip)

    val isToastShownByOs = Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2

    alertMessage
        .takeUnless { it.isEmpty() }
        ?.takeUnless { isToastShownByOs }
        ?.let { makeToast(it) }
}

fun Context.copyToClipBoard(
    text: String,
    alertMessageRes: Int?,
) {
    if (alertMessageRes == null) copyToClipBoard(text)
    else copyToClipBoard(text, getString(alertMessageRes))
}

fun Context.setLocale(locale: Locale?) {
    if (locale == null) return

    Locale.setDefault(locale)

    val config = resources.configuration.apply {
        setLocale(locale)
        setLayoutDirection(locale)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        createConfigurationContext(config)

    @Suppress("DEPRECATION")
    resources.updateConfiguration(config, resources.displayMetrics)
}
