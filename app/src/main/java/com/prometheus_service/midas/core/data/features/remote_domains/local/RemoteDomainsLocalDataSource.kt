package com.prometheus_service.midas.core.data.features.remote_domains.local

import com.prometheus_service.midas.core.domain.features.remote_domains.model.RemoteDomainsModel
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import kotlinx.coroutines.flow.Flow

interface RemoteDomainsLocalDataSource {
    fun getRemoteDomainsModel(): Flow<RemoteDomainsModel>
    suspend fun cacheRemoteDomainsModel(data: RemoteDomainsModel)
}