package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject


enum class EnrollmentResult {
    BIOMETRICS_NOT_ENROLLED,
    BIOMETRICS_FAILED
}

class HandleBiometricsEnrollment @Inject constructor(
    private val biometricsManager: BiometricsManager,
    private val cipherManager: CipherManager
) {

    suspend operator fun invoke(data: String): Result<EnrollmentResult> {

        Timber.d("Handling biometrics enrollment...")

        biometricsManager.setCurrentAccount(data).onFailure {
            Timber.d("Setting current account failed: $it")
            return Result.success(EnrollmentResult.BIOMETRICS_FAILED)
        }

        val canEnroll = biometricsManager.canEnrollBiometrics()
        if (!canEnroll) {
            Timber.d("User cannot enroll with biometrics")
            return Result.success(EnrollmentResult.BIOMETRICS_FAILED)
        }

        val username = biometricsManager.currentAccount?.memberCode
            ?: return Result.success(EnrollmentResult.BIOMETRICS_FAILED)
        
        return try {
            if (!biometricsManager.isUserEnrolled(username)) {
                Timber.d("User $username is not enrolled, starting enrollment flow...")
                Result.success(EnrollmentResult.BIOMETRICS_NOT_ENROLLED)
            } else {
                Timber.d("User $username is already enrolled.")
                Result.success(EnrollmentResult.BIOMETRICS_FAILED)
            }
        } catch (e: Exception) {
            Timber.e(e, "Biometrics enrollment failed")
            Result.success(EnrollmentResult.BIOMETRICS_FAILED)
        }
    }
}