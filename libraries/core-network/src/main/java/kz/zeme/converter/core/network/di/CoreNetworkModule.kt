package kz.zeme.converter.core.network.di

import org.koin.dsl.module
import kz.zeme.converter.core.network.extensions.createGson
import kz.zeme.converter.core.network.extensions.createOkHttpClient
import kz.zeme.converter.core.network.interceptors.DeviceDataFactory
import kz.zeme.converter.core.network.interceptors.DeviceInfoInterceptor
import kz.zeme.converter.core.network.interceptors.HttpErrorInterceptor
import kz.zeme.converter.core.network.interceptors.UserAgentInterceptor

val coreNetworkModule = module {
    single { DeviceDataFactory() }
    single { DeviceInfoInterceptor(deviceDataFactory = get()) }
    single { UserAgentInterceptor() }
    single { HttpErrorInterceptor() }

    single {
        createOkHttpClient(
            connectTimeoutSec = 3L,
            readTimeoutSec = 10L,
            writeTimeOutSec = 60L,
            userAgentInterceptor = get(),
            deviceInfoInterceptor = get(),
            httpErrorInterceptor = get(),
        )
    }

    single { createGson() }
}
