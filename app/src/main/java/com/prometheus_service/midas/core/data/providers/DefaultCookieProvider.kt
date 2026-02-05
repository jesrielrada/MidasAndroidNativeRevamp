package com.prometheus_service.midas.core.data.providers

import android.webkit.CookieManager
import com.prometheus_service.midas.core.domain.providers.CookieProvider
import javax.inject.Inject

class DefaultCookieProvider @Inject constructor() : CookieProvider {

    companion object {
        private const val PATH = "Path=/;"
        private const val MAX_AGE = "Max-Age=99999999;"
        private const val PRIORITY = "Priority=High;"
        private const val EXPIRY = "expires=Thu, 01 Jan 1970 00:00:00 UTC;"
        private const val DOMAIN = "Domain="
    }

    override fun initializeNativeCookies(
        domain: String,
        version: String,
        language: String
    ) {
        val languageCookie = "lang=$language; $PATH}"
        val versionCookie = "appVersion=$version; $PATH $MAX_AGE $PRIORITY"
        val nativeCookie = "is-native=2; $PATH $MAX_AGE $PRIORITY"
        CookieManager.getInstance().setCookie(domain, versionCookie)
        CookieManager.getInstance().setCookie(domain, nativeCookie)
        CookieManager.getInstance().setCookie(domain, languageCookie)
        CookieManager.getInstance().flush()
    }

}