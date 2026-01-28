package com.prometheus_service.midas.core.domain.features.splash_tutorial

import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import kotlinx.coroutines.flow.Flow

interface SplashTutorialRepository {
    val splashTutorialModel: Flow<SplashTutorialModel>
    suspend fun refreshSplashTutorialData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ) : Result<Unit>
}