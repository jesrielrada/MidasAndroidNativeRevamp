package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.providers.CookieProvider
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class InitializeNativeCookies @Inject constructor(
    private val cookieProvider: CookieProvider,
    private val getAppConfigModel: GetAppConfigModel
) {
    suspend operator fun invoke(
        domain: String,
        version: String,
        language: String
    ) {
        try {
            val sessionCookies = getAppConfigModel.invoke().first().sessionCookies
            if (sessionCookies != null) {
                //Temporarily remove delete cookie since its causing client error
                //cookieProvider.removeAllCookies()
                Timber.d("Deleting old cookies, initializing session cookies")
                cookieProvider.persistCookies()
                cookieProvider.initializeSessionCookies(
                    domain = domain,
                    cookies = sessionCookies
                )
            }

            Timber.d("Setting native cookies ..., domain: $domain")

            cookieProvider.initializeNativeCookies(
                domain = domain,
                version = version,
                language = language
            )

            delay(500) // Add delay so that cookies are updated
            cookieProvider.persistCookies()

        } catch (e: Exception) {
            Timber.e(e, "Error setting native cookies")
        }
    }
}