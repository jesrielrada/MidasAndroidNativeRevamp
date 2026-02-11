package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import androidx.biometric.BiometricPrompt
import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import timber.log.Timber
import javax.inject.Inject

sealed class AuthSucceedResult {
    object Authorized : AuthSucceedResult()
    object Failed : AuthSucceedResult()
}

class HandleAccountSelectedAuthSucceed @Inject constructor(
    private val cipherManager: CipherManager,
    private val biometricsManager: BiometricsManager
) {
    suspend operator fun invoke(result: BiometricPrompt.AuthenticationResult): Result<AuthSucceedResult> {
        try {
            val cipher = result.cryptoObject?.cipher
            val cipherTextWrapper = biometricsManager.cipherTextWrapper

            if (cipher != null && cipherTextWrapper != null) {
                val decryptedPassword = cipherManager.decryptData(cipherTextWrapper.ciphertext)
                biometricsManager.setCurrentAccount(CurrentAccount(password = decryptedPassword))
                return Result.success(AuthSucceedResult.Authorized)
            } else {
                return Result.success(AuthSucceedResult.Failed)
            }
        } catch (e: Exception) {
            Timber.e("Error handling account selected auth succeed")
            return Result.failure(e)
        }

    }
}