package com.prometheus_service.midas.core.data.shared.remote_config.local

import com.prometheus_service.midas.core.domain.shared.remote_config.model.RemoteConfigModel
import kotlinx.coroutines.flow.Flow

interface RemoteConfigLocalDataSource {

    fun getRemoteConfigModel(): Flow<RemoteConfigModel>
    suspend fun cacheRemoteConfigModel(model: RemoteConfigModel)

}