package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.SyncMultiLanguageData
import com.prometheus_service.midas.core.domain.shared.remote_domains.use_case.SyncRemoteDomains
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.SyncSplashTutorialImages
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SyncRemoteData @Inject constructor(
    private val dispatcherProvider: DefaultDispatcherProvider,
    private val getAppConfig: GetAppConfigModel,
    private val syncRemoteDomains: SyncRemoteDomains,
    private val syncSplashTutorialImages: SyncSplashTutorialImages,
    private val syncMultiLanguageData: SyncMultiLanguageData
) {
    //TODO(Check currency, check should fetch remote data, check force fetch data)
    suspend operator fun invoke(locale: String) {
        withContext(dispatcherProvider.io) {
            val config = getAppConfig.invoke().first()
            val userAgent = FlavorConfig.INITIAL_USER_AGENT
            val operatorId = FlavorConfig.OPERATOR_ID
            val currency = config.currency

            Timber.d("Syncing remote data ... locale is $locale, currency is $currency ")

            syncRemoteDomains.invoke(
                operatorId = operatorId,
                userAgent = userAgent,
                acceptLanguage = locale,
                currency = currency
            )

            Timber.d("Syncing splash and tutorial images")

            syncSplashTutorialImages.invoke(
                operatorId = operatorId,
                userAgent = userAgent,
                acceptLanguage = locale,
                currency = currency
            )

            Timber.d("Syncing multi language data")

            syncMultiLanguageData.invoke(
                operatorId = operatorId,
                userAgent = userAgent,
                acceptLanguage = locale,
                currency = currency
            )
        }
    }
}