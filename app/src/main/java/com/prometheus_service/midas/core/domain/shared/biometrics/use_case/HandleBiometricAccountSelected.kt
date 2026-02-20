package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import timber.log.Timber
import javax.crypto.Cipher
import javax.inject.Inject

sealed class AccountSelectedResult {
    data class Authorized(val cipher: Cipher) : AccountSelectedResult()
    object NonEnrolled : AccountSelectedResult()
}

class HandleBiometricAccountSelected @Inject constructor(
    private val biometricsManager: BiometricsManager,
    private val cipherManager: CipherManager,
    private val setBiometricsEnabled: SetBiometricsEnabled
) {
    suspend operator fun invoke(
        key: String,
        username: String,
        locale: String
    ): Result<AccountSelectedResult> {
        return try {
            val encryptedPassword = biometricsManager.getEncryptedPassword(username)
            if (encryptedPassword != null) {
                val vector = encryptedPassword.initializationVector

                biometricsManager.setCipherTextWrapper(encryptedPassword)
                biometricsManager.setCurrentAccount(CurrentAccount(memberCode = username))

                cipherManager.setCipherMode(Cipher.DECRYPT_MODE, key, vector)
                val cipher = cipherManager.cipher

                if (cipher != null) {
                    Result.success(AccountSelectedResult.Authorized(cipher))
                } else if (!biometricsManager.isUserEnrolled(username)) {
                    biometricsManager.setCurrentAccount(CurrentAccount()) //to clear account
                    biometricsManager.deleteAllAccounts()
                    Result.success(AccountSelectedResult.NonEnrolled)
                } else {
                    biometricsManager.setCurrentAccount(CurrentAccount()) //to clear account
                    biometricsManager.deleteAllAccounts()
                    setBiometricsEnabled.invoke(locale, false)
                    throw Exception("Cipher is null")
                }
            }else {
                throw Exception("Encrypted password is null")
            }
        } catch (e: Exception) {
            Timber.d("Failure handling biometric account selected ${e.localizedMessage}")
            Result.failure(e)
        }
    }
}