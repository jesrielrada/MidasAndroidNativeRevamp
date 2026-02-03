package com.prometheus_service.midas.core.data.shared.multi_language.remote

import com.prometheus_service.midas.core.data.shared.multi_language.remote.model.MultiLanguageDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface MultiLanguageService {
    @GET("/cms/api/v3/widgets/native_multi_language_support")
    suspend fun fetchMultiLanguageSupport(
        @Header("Prometheus-Operator-Id") operatorId: String,
        @Header("Accept-Language") language: String,
        @Header("User-Agent") userAgent: String,
        @Header("Prometheus-Currency") currency: String?,
    ): Response<MultiLanguageDto>
}