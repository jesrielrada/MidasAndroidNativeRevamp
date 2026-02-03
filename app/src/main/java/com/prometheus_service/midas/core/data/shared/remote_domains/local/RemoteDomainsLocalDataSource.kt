package com.prometheus_service.midas.core.data.shared.remote_domains.local

import com.prometheus_service.midas.core.domain.shared.remote_domains.model.RemoteDomainsModel
import kotlinx.coroutines.flow.Flow

interface RemoteDomainsLocalDataSource {
    fun getRemoteDomainsModel(): Flow<RemoteDomainsModel>
    suspend fun cacheRemoteDomainsModel(data: RemoteDomainsModel)
}