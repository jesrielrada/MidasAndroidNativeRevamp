package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import androidx.biometric.BiometricPrompt
import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import timber.log.Timber
import javax.inject.Inject

class PersistBiometricsUser @Inject constructor(
    private val biometricsManager: BiometricsManager,
    private val cipherManager: CipherManager
) {
    suspend operator fun invoke(result: BiometricPrompt.AuthenticationResult): Result<Unit> {
        val account = biometricsManager.currentAccount ?: return Result.failure(Exception("No current account found"))
        result.cryptoObject?.cipher ?: return Result.failure(Exception("No cipher found"))

        return try {


            val username =
                account.memberCode ?: return Result.failure(Exception("Username is null"))
            val password = account.password ?: return Result.failure(Exception("Password is null"))

            val encryptedPassword = cipherManager.encryptData(password)

            val result = biometricsManager.persistAccount(
                username = username,
                encryptedPassword = encryptedPassword
            )

            if (result.isSuccess) {
                Timber.d("Persisting biometrics user success")
                Result.success(Unit)
            } else {
                val error =
                    result.exceptionOrNull() ?: Exception("Unknown error during persistence")
                Timber.e(error, "Persisting biometrics user failed")
                Result.failure(error)
            }
        } catch (e: Exception) {
            Timber.e(e, "Unexpected error in PersistBiometricsUser")
            Result.failure(e)
        }
    }

}