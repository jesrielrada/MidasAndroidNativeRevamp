package com.prometheus_service.midas.core.data.features.app_config.remote

import com.prometheus_service.midas.core.data.features.app_config.remote.model.AppConfigDto

interface AppConfigRemoteDataSource {
    suspend fun fetchApplicationConfig(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
    ): AppConfigDto
}