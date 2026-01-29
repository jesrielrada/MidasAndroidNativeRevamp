package com.prometheus_service.midas.core.domain.features.remote_config

import com.prometheus_service.midas.core.domain.features.remote_config.model.RemoteConfigModel
import kotlinx.coroutines.flow.Flow

interface RenameConfigRepository {

    val remoteConfigModel: Flow<RemoteConfigModel>

    suspend fun refreshRemoteConfigData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
    ) : Result<Unit>
}