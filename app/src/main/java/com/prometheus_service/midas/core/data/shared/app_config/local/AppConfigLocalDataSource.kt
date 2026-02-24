package com.prometheus_service.midas.core.data.shared.app_config.local

import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import kotlinx.coroutines.flow.Flow

interface AppConfigLocalDataSource {

    fun getAppConfigModel(): Flow<AppConfigModel>
    suspend fun cacheAppConfigModel(data: AppConfigModel)

    suspend fun deleteSessionCookies()
    suspend fun deleteBestDomain()

}