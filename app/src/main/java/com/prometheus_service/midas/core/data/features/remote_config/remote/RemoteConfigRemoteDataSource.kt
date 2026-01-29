package com.prometheus_service.midas.core.data.features.remote_config.remote

import com.prometheus_service.midas.core.data.features.remote_config.remote.model.RemoteConfigDto

interface RemoteConfigRemoteDataSource {
    suspend fun fetchRemoteConfig(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
    ): RemoteConfigDto
}