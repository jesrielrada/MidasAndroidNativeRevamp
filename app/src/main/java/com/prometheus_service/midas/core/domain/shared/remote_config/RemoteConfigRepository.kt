package com.prometheus_service.midas.core.domain.shared.remote_config

import com.prometheus_service.midas.core.domain.shared.remote_config.model.RemoteConfigModel
import kotlinx.coroutines.flow.Flow

interface RemoteConfigRepository {

    val remoteConfigModel: Flow<RemoteConfigModel>

    suspend fun syncRemoteConfigData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
    ) : Result<RemoteConfigModel>
}