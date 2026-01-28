package com.prometheus_service.midas.core.data.features.remote_domains.remote

import com.prometheus_service.midas.core.data.features.remote_domains.remote.model.RemoteDomainsDto

interface RemoteDomainsRemoteDataSource {
    suspend fun fetchRemoteDomainsData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): RemoteDomainsDto
}