package com.prometheus_service.midas.core.domain.features.biometrics.manager

import androidx.biometric.BiometricManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CipherTextWrapper
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import kotlinx.coroutines.flow.Flow

interface BiometricsManager {

    val biometricsManager: BiometricManager
    val isNoneEnrolledBiometrics: Boolean
    val currentAccount: CurrentAccount?
    val cipherTextWrapper: CipherTextWrapper?
    suspend fun setCipherTextWrapper(cipherTextWrapper: CipherTextWrapper)
    suspend fun getEncryptedPassword(username: String): CipherTextWrapper?
    suspend fun getUsernames(): List<String>?
    suspend fun isBiometricsEnabled(): Boolean
    suspend fun setBiometricsCmsboEnabled(cmsboEnabled: Boolean)
    suspend fun setCurrentAccount(data: String): Result<Unit>
    suspend fun setCurrentAccount(currentAccount: CurrentAccount)
    suspend fun canAuthenticateBiometrics(): Flow<Boolean>
    suspend fun canDisplayBiometrics(): Flow<Boolean>
    suspend fun canEnrollBiometrics(): Boolean
    suspend fun isUserEnrolled(username: String): Boolean
    suspend fun persistAccount(
        username: String,
        encryptedPassword: CipherTextWrapper?
    ): Result<Unit>

    suspend fun deleteAccount(username: String)

    suspend fun deleteAllAccounts()

}