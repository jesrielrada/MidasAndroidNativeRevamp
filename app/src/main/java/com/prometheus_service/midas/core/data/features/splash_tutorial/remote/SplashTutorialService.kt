package com.prometheus_service.midas.core.data.features.splash_tutorial.remote

import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.model.SplashTutorialDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface SplashTutorialService {
    @GET("/cms/api/v3/widgets/native_splash_images")
    suspend fun fetchSplashTutorialRemoteData(
        @Header("Prometheus-Operator-Id") operatorId: String,
        @Header("User-Agent") userAgent: String,
        @Header("Accept-Language") acceptLanguage: String,
        @Header("prometheus-currency") currency: String?
    ): Response<SplashTutorialDto>
}