package com.prometheus_service.midas.core.domain.features.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import javax.inject.Inject

class GetBiometricCurrentAccount @Inject constructor(
    private val biometricsManager: BiometricsManager
) {
    operator fun invoke(): CurrentAccount? {
        return biometricsManager.currentAccount
    }
}