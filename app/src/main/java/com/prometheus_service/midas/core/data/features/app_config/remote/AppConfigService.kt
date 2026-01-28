package com.prometheus_service.midas.core.data.features.app_config.remote

import com.prometheus_service.midas.core.data.features.app_config.remote.model.AppConfigDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface AppConfigService {

    @GET("/cms/api/v3/mobile/native/list")
    suspend fun fetchApplicationConfigRemoteData(
        @Header("Prometheus-Operator-Id") operatorId: String,
        @Header("User-Agent") userAgent: String,
        @Header("Accept-Language") acceptLanguage: String,
    ): Response<AppConfigDto>
}