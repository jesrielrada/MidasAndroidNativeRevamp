package com.prometheus_service.midas.core.domain.features.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import javax.inject.Inject

class HandleBiometricAuthCancelled @Inject constructor(
    private val biometricManager: BiometricsManager,
    private val setBiometricsEnabled: SetBiometricsEnabled,
) {
    suspend operator fun invoke(locale: String): Result<Unit> {
        return try {
            if (biometricManager.isNoneEnrolledBiometrics) {
                biometricManager.setCurrentAccount(CurrentAccount()) //clear account
                biometricManager.deleteAllAccounts()
                setBiometricsEnabled.invoke(locale, false)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failure handling biometric auth dismissed"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e))
        }
    }
}