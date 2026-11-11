package kz.zeme.converter.core.activity.extensions

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.system.exitProcess

fun Activity.isPermissionGranted(
    vararg permission: String
): Boolean {
    if (permission.isEmpty()) return false

    permission.forEach {
        val result = ContextCompat.checkSelfPermission(this, it)
        if (result == PackageManager.PERMISSION_GRANTED) Unit
        else return false
    }

    return true
}

fun Activity.closeApp() {
    finishAffinity()
    exitProcess(0)
}

fun Activity.enableEdgeToEdge() {
    val systemBarStyle =
        if (isConverter()) SystemBarStyle.dark(Color.TRANSPARENT)
        else SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)

    (this as? AppCompatActivity)?.enableEdgeToEdge(
        statusBarStyle = systemBarStyle,
        navigationBarStyle = systemBarStyle,
    )
}

fun Activity.isConverter(): Boolean =
    componentName.className.startsWith("kz.zeme")

fun Activity.isAbr(): Boolean = isConverter()
