package com.prometheus_service.midas.core.data.features.remote_config.remote

import com.prometheus_service.midas.core.data.features.remote_config.remote.model.RemoteConfigDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface RemoteConfigService {
    @GET("/cms/api/v3/mobile/native/list")
    suspend fun fetchRemoteConfigRemoteData(
        @Header("Prometheus-Operator-Id") operatorId: String,
        @Header("User-Agent") userAgent: String,
        @Header("Accept-Language") acceptLanguage: String,
    ): Response<RemoteConfigDto>
}