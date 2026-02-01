package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.features.remote_config.use_case.GetRemoteConfig
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FetchAppBaseUrl @Inject constructor(
    private val getAppConfig: GetAppConfigModel,
    private val getRemoteConfig: GetRemoteConfig,
    private val dispatcherProvider: DefaultDispatcherProvider
) {
    suspend operator fun invoke(): String? {
        return withContext(dispatcherProvider.io) {

            Timber.d("Retrieving app config...")

            val config = getAppConfig.invoke().first()
            val locale = config.locale ?: FlavorConfig.DEFAULT_LOCALE
            val userAgent = FlavorConfig.INITIAL_USER_AGENT
            val operatorId = FlavorConfig.OPERATOR_ID

            Timber.d("Building initial user agent.. $userAgent")

            val result = getRemoteConfig.invoke(
                operatorId = operatorId,
                userAgent = userAgent,
                acceptLanguage = locale,
            )

            result.fold(
                onSuccess = { config ->
                    Timber.d("Success fetching app base url")
                    config.domainPwa?.firstOrNull()
                },
                onFailure = { error ->
                    Timber.e(error, "Failed to fetch app base url")
                    error.toString()
                }
            )
        }
    }
}