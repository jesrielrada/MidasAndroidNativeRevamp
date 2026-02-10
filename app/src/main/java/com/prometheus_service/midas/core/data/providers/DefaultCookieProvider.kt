package com.prometheus_service.midas.core.data.providers

import android.webkit.CookieManager
import com.prometheus_service.midas.core.domain.providers.CookieProvider
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultCookieProvider @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) : CookieProvider {

    companion object {
        private const val PATH = "Path=/;"
        private const val MAX_AGE = "Max-Age=99999999;"
        private const val PRIORITY = "Priority=High;"
        private const val EXPIRY = "expires=Thu, 01 Jan 1970 00:00:00 UTC;"
        private const val DOMAIN = "Domain="
    }

    override suspend fun initializeNativeCookies(
        domain: String,
        version: String,
        language: String
    ) {
        withContext(dispatcherProvider.io) {
            val languageCookie = "lang=$language; $PATH"
            val versionCookie = "appVersion=$version; $PATH $MAX_AGE $PRIORITY"
            val nativeCookie = "is-native=2; $PATH $MAX_AGE $PRIORITY"
            CookieManager.getInstance().setCookie(domain, versionCookie)
            CookieManager.getInstance().setCookie(domain, nativeCookie)
            CookieManager.getInstance().setCookie(domain, languageCookie)
            CookieManager.getInstance().flush()
        }
    }

    override suspend fun isLoggedIn(baseUrl: String): Boolean {
        return withContext(dispatcherProvider.io) {
            try {
                CookieManager.getInstance().getCookie(baseUrl).contains("pt_token")
            } catch (e: Exception) {
                Timber.d("Error getting isLoggedIn cookie: ${e.message}")
                false
            }
        }
    }

    override suspend fun persistCookies() {
        withContext(dispatcherProvider.io) {
            CookieManager.getInstance().flush()
        }
    }
}