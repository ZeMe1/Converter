package kz.zeme.converter.core.network.interceptors

import android.os.Build

class DeviceDataFactory {

    fun getPlatform(): String = "android"

    fun getDeviceModel(): String = Build.MODEL

    fun getDeviceOs(): String = Build.VERSION.SDK_INT.toString()
}
