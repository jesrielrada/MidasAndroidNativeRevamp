package com.prometheus_service.midas.core.data.features.remote_domains.remote

import com.prometheus_service.midas.core.data.features.remote_domains.remote.model.RemoteDomainsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface RemoteDomainsService {
    @GET("/cms/api/v3/widgets/native_remote_domains")
    suspend fun fetchRemoteDomainsData(
        @Header("Prometheus-Operator-Id") operatorId: String,
        @Header("Accept-Language") acceptLanguage: String,
        @Header("User-Agent") userAgent: String,
        @Header("Prometheus-Currency") currency: String?
    ) : Response<RemoteDomainsDto>
}

