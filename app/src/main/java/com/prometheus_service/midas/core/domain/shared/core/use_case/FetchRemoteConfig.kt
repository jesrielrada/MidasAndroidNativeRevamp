package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.shared.remote_config.use_case.SyncRemoteConfig
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.shared.remote_config.model.RemoteConfigModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class FetchRemoteConfig @Inject constructor(
    private val getAppConfig: GetAppConfigModel,
    private val syncRemoteConfig: SyncRemoteConfig,
    private val setHostInterceptorUrl: SetHostInterceptorUrl,
    private val dispatcherProvider: DefaultDispatcherProvider
) {
    suspend operator fun invoke(
        operatorId: String = FlavorConfig.OPERATOR_ID,
        userAgent: String = FlavorConfig.INITIAL_USER_AGENT,
        defaultLocale: String = FlavorConfig.DEFAULT_LOCALE,
        domains: List<String>
    ): Pair<String, RemoteConfigModel?>? {
        return withContext(dispatcherProvider.io) {
            domains.firstNotNullOfOrNull { domain ->
                try {
                    Timber.d("Trying domain... $domain")
                    setHostInterceptorUrl(domain)
                    // 2. Inline variables that are only used once
                    val locale = getAppConfig().first().locale ?: defaultLocale
                    // 3. Chain the result cleanly. If this yields a String, the loop stops!
                    val result = syncRemoteConfig(
                        operatorId = operatorId,
                        userAgent = userAgent,
                        acceptLanguage = locale
                    )
                    domain to result
                } catch (e: CancellationException) {
                    // 4. CRITICAL MODERN STANDARD: Always rethrow CancellationException
                    // so parent coroutines (like ViewModels) can cancel properly.
                    throw e
                } catch (e: Exception) {
                    Timber.e(e, "Failed to fetch app base url for $domain")
                    // 5. Return null for this iteration so it moves to the next domain
                    null
                }
            }
        }
    }
}