package com.prometheus_service.midas.core.data.features.splash_tutorial.local

import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import kotlinx.coroutines.flow.Flow

interface SplashTutorialLocalDataSource {
    fun getSplashTutorialModel(): Flow<SplashTutorialModel>
    suspend fun cacheSplashTutorialModel(data: SplashTutorialModel)
}