package com.prometheus_service.midas.core.domain.features.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import javax.crypto.Cipher
import javax.inject.Inject

class InitializeBiometricsPrompt @Inject constructor(
    private val cipherManager: CipherManager,
    private val biometricsManager: BiometricsManager
) {

    suspend operator fun invoke(key: String): Result<Cipher?> {
        if (biometricsManager.isBiometricsEnabled()) {
            cipherManager.setCipherMode(
                mode = Cipher.ENCRYPT_MODE,
                key = key,
                vector = null
            )
            val cipher = cipherManager.cipher

            return if (cipherManager.cipher != null) {
                Result.success(cipher)
            } else {
                Result.failure(Exception("Cipher is null"))
            }

        }
        return Result.failure(Exception("Biometrics are not enabled"))
    }
}