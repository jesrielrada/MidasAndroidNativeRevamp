package com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case

import com.prometheus_service.midas.core.domain.features.splash_tutorial.SplashTutorialRepository
import timber.log.Timber
import javax.inject.Inject

class SyncSplashTutorialImages @Inject constructor(
    private val repository: SplashTutorialRepository
) {
    suspend operator fun invoke(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ) {
        val result = repository.syncSplashTutorialData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
            currency = currency
        )
        Timber.d("Splash tutorial images: $result")
    }
}