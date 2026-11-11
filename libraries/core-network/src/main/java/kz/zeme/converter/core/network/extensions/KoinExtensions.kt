package kz.zeme.converter.core.network.extensions

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kz.zeme.converter.core.network.DnsTimeout
import kz.zeme.converter.core.network.EnumRetrofitConverterFactory
import kz.zeme.converter.core.network.interceptors.DeviceInfoInterceptor
import kz.zeme.converter.core.network.interceptors.HttpErrorInterceptor
import kz.zeme.converter.core.network.interceptors.UserAgentInterceptor
import okhttp3.OkHttpClient
import org.koin.core.scope.Scope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

inline fun <reified T : Any> Scope.createWebService(
    baseUrl: String,
    gson: Gson = get(),
    okHttpClient: OkHttpClient = get(),
): T = createWebService(
    baseUrl = baseUrl,
    service = T::class,
    gson = gson,
    okHttpClient = okHttpClient,
)

fun <T : Any> createWebService(
    baseUrl: String,
    service: KClass<T>,
    gson: Gson,
    okHttpClient: OkHttpClient,
): T = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .addConverterFactory(EnumRetrofitConverterFactory())
    .build()
    .create(service.java)

fun createOkHttpClient(
    connectTimeoutSec: Long,
    readTimeoutSec: Long,
    writeTimeOutSec: Long,
    userAgentInterceptor: UserAgentInterceptor,
    deviceInfoInterceptor: DeviceInfoInterceptor,
    httpErrorInterceptor: HttpErrorInterceptor,
): OkHttpClient {
    val okHttpBuilder = OkHttpClient.Builder()
        .connectTimeout(connectTimeoutSec, TimeUnit.SECONDS)
        .readTimeout(readTimeoutSec, TimeUnit.SECONDS)
        .writeTimeout(writeTimeOutSec, TimeUnit.SECONDS)
        .proxy(Proxy.NO_PROXY)
        .dns(DnsTimeout(timeoutMs = connectTimeoutSec * 1000L))
        .retryOnConnectionFailure(false)
        .addInterceptor(httpErrorInterceptor)
        .addInterceptor(deviceInfoInterceptor)
        .addInterceptor(userAgentInterceptor)

    return okHttpBuilder.build()
}

fun createGson(
    applyExtraSettings: (GsonBuilder) -> Unit = {},
): Gson {
    val builder = GsonBuilder()
    applyExtraSettings(builder)
    return builder.create()
}
