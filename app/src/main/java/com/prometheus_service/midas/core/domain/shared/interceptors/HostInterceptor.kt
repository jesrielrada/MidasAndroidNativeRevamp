package com.prometheus_service.midas.core.domain.shared.interceptors

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HostInterceptor @Inject constructor() : Interceptor {
    private var mScheme: String? = null
    private var mHost: String? = null

    fun getBaseUrl(): String? {
        val formattedUrl =
            if (mHost != null &&
                !mHost?.startsWith("http://")!! &&
                !mHost!!.startsWith("https://")) {
                "https://$mHost"
            } else {
                mHost
            }
        return formattedUrl
    }

    fun setUrl(url: String) {
        val httpUrl: HttpUrl = url.toHttpUrl()
        mScheme = httpUrl.scheme
        mHost = httpUrl.host
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var original: Request = chain.request()
        // If new Base URL is properly formatted then replace with old one
        if (mScheme != null && mHost != null) {
            val newUrl: HttpUrl = original.url.newBuilder()
                .scheme(mScheme!!)
                .host(mHost!!)
                .build()
            original = original.newBuilder()
                .url(newUrl)
                .build()
        }
        return chain.proceed(original)
    }
}