package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import javax.crypto.Cipher
import javax.inject.Inject

sealed class AccountSelectedResult {
    data class Authorized(val cipher: Cipher) : AccountSelectedResult()
    object NonEnrolled : AccountSelectedResult()
    object Failed : AccountSelectedResult()
}

class HandleBiometricAccountSelected @Inject constructor(
    private val biometricsManager: BiometricsManager,
    private val cipherManager: CipherManager,
) {
    suspend operator fun invoke(key: String, username: String): Result<AccountSelectedResult> {
        val encryptedPassword = biometricsManager.getEncryptedPassword(username)
        if (encryptedPassword != null) {
            val vector = encryptedPassword.initializationVector

            biometricsManager.setCipherTextWrapper(encryptedPassword)
            biometricsManager.setCurrentAccount(CurrentAccount(memberCode = username))

            cipherManager.setCipherMode(Cipher.DECRYPT_MODE, key, vector)
            val cipher = cipherManager.cipher

            return if (cipher != null) {
                Result.success(AccountSelectedResult.Authorized(cipher))
            } else if (!biometricsManager.isUserEnrolled(username)) {
                Result.success(AccountSelectedResult.NonEnrolled)
            } else {
                Result.success(AccountSelectedResult.Failed)
            }
        }
        return Result.success(AccountSelectedResult.Failed)
    }
}