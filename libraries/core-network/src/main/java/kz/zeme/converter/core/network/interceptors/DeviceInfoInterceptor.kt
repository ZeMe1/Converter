package kz.zeme.converter.core.network.interceptors

import kz.zeme.converter.core.network.Headers
import okhttp3.Interceptor
import okhttp3.Response

class DeviceInfoInterceptor(
    private val deviceDataFactory: DeviceDataFactory,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(Headers.XPlatform, deviceDataFactory.getPlatform())
            .header(Headers.XDeviceModel, deviceDataFactory.getDeviceModel())
            .header(Headers.XDeviceOs, deviceDataFactory.getDeviceOs())
            .build()
        return chain.proceed(request)
    }
}
