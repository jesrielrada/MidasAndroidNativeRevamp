package com.prometheus_service.midas.core.domain.shared.remote_config.use_case

import com.prometheus_service.midas.core.domain.shared.remote_config.RemoteConfigRepository
import com.prometheus_service.midas.core.domain.shared.remote_config.model.RemoteConfigModel
import javax.inject.Inject

class SyncRemoteConfig @Inject constructor(
    private val repository: RemoteConfigRepository
) {
    suspend operator fun invoke(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
    ): RemoteConfigModel? {
        return repository.syncRemoteConfigData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
        ).getOrNull()
    }
}