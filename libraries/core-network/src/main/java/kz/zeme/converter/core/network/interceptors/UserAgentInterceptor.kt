package kz.zeme.converter.core.network.interceptors

import kz.zeme.converter.core.network.Headers
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor(
    private val userAgent: String = "Converter-Android",
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(Headers.UserAgent, userAgent)
            .build()
        return chain.proceed(request)
    }
}
