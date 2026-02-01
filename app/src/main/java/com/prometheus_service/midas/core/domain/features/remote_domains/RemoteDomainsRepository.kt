package com.prometheus_service.midas.core.domain.features.remote_domains

import com.prometheus_service.midas.core.domain.features.remote_domains.model.RemoteDomainsModel
import kotlinx.coroutines.flow.Flow

interface RemoteDomainsRepository {

    val remoteDomainsModel: Flow<RemoteDomainsModel>

    suspend fun syncRemoteDomainsData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ) : Result<Unit>
}