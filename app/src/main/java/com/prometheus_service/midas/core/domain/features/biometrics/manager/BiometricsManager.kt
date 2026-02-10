package com.prometheus_service.midas.core.domain.features.biometrics.manager

import androidx.biometric.BiometricManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CipherTextWrapper
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import kotlinx.coroutines.flow.Flow

interface BiometricsManager {

    val biometricsManager: BiometricManager
    val isNoneEnrolledBiometrics: Boolean
    val currentAccount: CurrentAccount?

    suspend fun isBiometricsEnabled(): Boolean
    suspend fun setBiometricsEnabled(cmsboEnabled: Boolean)
    suspend fun setCurrentAccount(data: String): Result<Unit>
    suspend fun canAuthenticateBiometrics(): Flow<Boolean>
    suspend fun canDisplayBiometrics(): Flow<Boolean>
    suspend fun canEnrollBiometrics(): Boolean
    suspend fun isUserEnrolled(username: String): Boolean
    suspend fun persistAccount(username: String, encryptedPassword: CipherTextWrapper?) : Result<Unit>

}