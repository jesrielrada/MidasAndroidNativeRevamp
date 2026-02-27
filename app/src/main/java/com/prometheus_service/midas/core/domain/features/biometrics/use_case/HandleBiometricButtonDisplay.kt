package com.prometheus_service.midas.core.domain.features.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class HandleBiometricButtonDisplay @Inject constructor(
    private val biometricsManager: BiometricsManager
) {
    suspend operator fun invoke(): Result<Boolean> {
        return try {
            val result = biometricsManager.canDisplayBiometrics().first()
            if (result) {
                Result.success(true)
            } else {
                Result.failure(Exception("Biometrics not enabled"))
            }
        } catch (e: Exception) {
            Timber.e("Error handling biometric button display: $e")
            Result.failure(e)
        }
    }
}


