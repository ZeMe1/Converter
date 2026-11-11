package kz.zeme.converter.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class HttpErrorInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}
