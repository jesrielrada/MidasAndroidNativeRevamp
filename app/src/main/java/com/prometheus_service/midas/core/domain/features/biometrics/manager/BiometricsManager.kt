package com.prometheus_service.midas.core.domain.features.biometrics.manager

import androidx.biometric.BiometricManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import kotlinx.coroutines.flow.Flow

interface BiometricsManager {

    val biometricsManager: BiometricManager
    val isNoneEnrolledBiometrics: Boolean

    val currentAccount: CurrentAccount?

    fun setCurrentAccount(username: String, password: String)

    suspend fun canAuthenticateBiometrics(): Flow<Boolean>

    suspend fun canDisplayBiometrics(): Flow<Boolean>


}