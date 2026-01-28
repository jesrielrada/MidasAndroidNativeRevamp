package com.prometheus_service.midas.core.domain.features.app_config

import com.prometheus_service.midas.core.domain.features.app_config.model.AppConfigModel
import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {

    val appConfigModel: Flow<AppConfigModel>

    suspend fun refreshAppConfigData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
    ) : Result<Unit>
}