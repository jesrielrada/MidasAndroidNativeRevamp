package com.prometheus_service.midas.core.data.features.app_config.local

import com.prometheus_service.midas.core.domain.features.app_config.model.AppConfigModel
import kotlinx.coroutines.flow.Flow

interface AppConfigLocalDataSource {

    fun getAppConfigModel(): Flow<AppConfigModel>
    suspend fun cacheAppConfigModel(model: AppConfigModel)

}