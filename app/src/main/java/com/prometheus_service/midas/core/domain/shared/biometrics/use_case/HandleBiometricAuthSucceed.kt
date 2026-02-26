package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import androidx.biometric.BiometricPrompt
import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import javax.inject.Inject


class HandleAccountSelectedAuthSucceed @Inject constructor(
    private val cipherManager: CipherManager,
    private val biometricsManager: BiometricsManager
) {
    suspend operator fun invoke(result: BiometricPrompt.AuthenticationResult): Result<Unit> {
        try {
            val cipher = result.cryptoObject?.cipher
            val cipherTextWrapper = biometricsManager.cipherTextWrapper

            if (cipher != null && cipherTextWrapper != null) {
                val decryptedPassword = cipherManager.decryptData(cipherTextWrapper.ciphertext)
                biometricsManager.setCurrentAccount(CurrentAccount(bio = decryptedPassword))
                return Result.success(Unit)
            } else {
                return Result.failure(Exception("Cipher or cipher text wrapper is null"))
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}