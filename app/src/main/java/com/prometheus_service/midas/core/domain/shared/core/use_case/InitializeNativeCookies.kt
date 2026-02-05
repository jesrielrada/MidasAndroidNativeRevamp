package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.providers.CookieProvider
import javax.inject.Inject

class InitializeNativeCookies @Inject constructor(
    private val cookieProvider: CookieProvider
) {
    operator fun invoke(
        domain: String,
        version: String,
        language: String
    ) {
        cookieProvider.initializeNativeCookies(
            domain = domain,
            version = version,
            language = language
        )
    }
}