package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import timber.log.Timber
import javax.crypto.Cipher
import javax.inject.Inject

sealed class EnrollmentResult {
    data class UpdateEnrollment(val cipher: Cipher) : EnrollmentResult()
    object FailedUpdateEnrollment : EnrollmentResult()
    object NonEnrolled : EnrollmentResult()
}

class HandleBiometricsEnrollment @Inject constructor(
    private val biometricsManager: BiometricsManager,
    private val cipherManager: CipherManager
) {
    companion object {
        private const val CHANGE_PASSWORD_ROUTE = "change-password-route"
        private const val SECURITY_ROUTE = "security-route"
    }

    suspend operator fun invoke(
        data: String?,
        currentRoute: String?,
        key: String
    ): Result<EnrollmentResult> {
        Timber.d("Handling biometrics enrollment")
        return try {
            val remoteData =
                data ?: return Result.failure(Exception("Remote data for biometrics is null"))

            biometricsManager.setCurrentAccount(remoteData).onFailure {
                return Result.failure(Exception("Failed to set current account"))
            }

            if (!biometricsManager.canEnrollBiometrics()) {
                return Result.failure(Exception("Biometrics enrollment not available"))
            }

            val username = biometricsManager.currentAccount?.memberCode ?: return Result.failure(
                Exception("Member code is null on biometric manager")
            )

            val isUpdateRoute = currentRoute.equals(SECURITY_ROUTE) ||
                    currentRoute.equals(CHANGE_PASSWORD_ROUTE)
            val isAccountUpdated = biometricsManager.isUserEnrolled(username) && isUpdateRoute

            if (!biometricsManager.isUserEnrolled(username)) {
                Result.success(EnrollmentResult.NonEnrolled)
            } else if (isAccountUpdated) {
                cipherManager.setCipherMode(Cipher.ENCRYPT_MODE, key, null)
                val cipher = cipherManager.cipher
                if (cipher != null) {
                    Result.success(EnrollmentResult.UpdateEnrollment(cipher))
                } else {
                    Result.success(EnrollmentResult.FailedUpdateEnrollment)
                }
            } else {
                Result.failure(Exception("User is already enrolled"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}