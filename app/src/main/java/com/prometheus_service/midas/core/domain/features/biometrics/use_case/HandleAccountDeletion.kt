package com.prometheus_service.midas.core.domain.features.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import javax.inject.Inject

class HandleAccountDeletion @Inject constructor(
    private val biometricsManager: BiometricsManager
) {
    suspend operator fun invoke(data: String) {
        biometricsManager.deleteAccount(data)
    }
}