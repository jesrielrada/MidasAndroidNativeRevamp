package com.prometheus_service.midas.core.domain.shared.app_config

import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {
    val appConfigModel: Flow<AppConfigModel>

    suspend fun cacheAppConfigModel(model: AppConfigModel) : Result<Unit>

    suspend fun cacheAppCurrency(data: String)

    suspend fun deleteCookies()
}