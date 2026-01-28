package com.prometheus_service.midas.core.domain.features.app_config.use_case

import com.prometheus_service.midas.core.domain.features.app_config.AppConfigRepository
import timber.log.Timber
import javax.inject.Inject

class GetApplicationConfig @Inject constructor(
    private val repository: AppConfigRepository
) {
    suspend operator fun invoke(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
    ) {
        val result = repository.refreshAppConfigData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
        )
        Timber.d("Application config model: $result}")
    }
}