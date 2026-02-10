package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.providers.CookieProvider
import javax.inject.Inject

class PersistNativeCookies @Inject constructor(
    private val cookieProvider: CookieProvider
) {

    suspend operator fun invoke() {
        cookieProvider.persistCookies()
    }
}