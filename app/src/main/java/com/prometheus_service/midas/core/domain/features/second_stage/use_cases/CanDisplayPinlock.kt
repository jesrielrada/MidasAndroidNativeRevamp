package com.prometheus_service.midas.core.domain.features.second_stage.use_cases

import com.prometheus_service.midas.core.domain.features.second_stage.repository.SecondStageRepository
import com.prometheus_service.midas.core.domain.providers.CookieProvider
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CanDisplayPinlock @Inject constructor(
    private val secondStageRepository: SecondStageRepository,
    private val cookieProvider: CookieProvider
) {

    suspend operator fun invoke(
        baseUrl: String?,
        pwaReady: Boolean,
    ): Boolean {
        val model = secondStageRepository.secondStageModel.first()
        val hasCachedPin = model.pin != null && model.pin != ""
        val isUserEnabled = model.isUserEnabled != null && model.isUserEnabled
        val isCmsboEnabled = model.isCmsboEnabled != null && model.isCmsboEnabled
        val isLoggedIn = baseUrl != null && cookieProvider.isLoggedIn(baseUrl)
        return hasCachedPin && isUserEnabled && isCmsboEnabled && isLoggedIn && pwaReady
    }
}