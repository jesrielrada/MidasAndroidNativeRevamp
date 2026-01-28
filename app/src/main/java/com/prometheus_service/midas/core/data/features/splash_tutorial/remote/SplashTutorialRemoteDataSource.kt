package com.prometheus_service.midas.core.data.features.splash_tutorial.remote

import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.model.SplashTutorialDto

interface SplashTutorialRemoteDataSource {
    suspend fun fetchSplashTutorialData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): SplashTutorialDto
}