package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.providers.CookieProvider
import kotlinx.coroutines.delay
import javax.inject.Inject

class InitializeNativeCookies @Inject constructor(
    private val cookieProvider: CookieProvider
) {
    suspend operator fun invoke(
        domain: String,
        version: String,
        language: String
    ) {
        cookieProvider.initializeNativeCookies(
            domain = domain,
            version = version,
            language = language
        )
        delay(500) // Add delay so that cookies are updated
        cookieProvider.persistCookies()
    }
}