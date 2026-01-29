package com.prometheus_service.midas.core.domain.features.remote_config.use_case

import com.prometheus_service.midas.core.domain.features.remote_config.RenameConfigRepository
import timber.log.Timber
import javax.inject.Inject

class GetRemoteConfig @Inject constructor(
    private val repository: RenameConfigRepository
) {
    suspend operator fun invoke(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
    ) {
        val result = repository.refreshRemoteConfigData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
        )
        Timber.d("Remote config model: $result}")
    }
}