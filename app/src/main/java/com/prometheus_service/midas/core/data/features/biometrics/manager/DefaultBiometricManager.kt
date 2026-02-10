package com.prometheus_service.midas.core.data.features.biometrics.manager

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import com.prometheus_service.midas.core.domain.features.biometrics.repository.BiometricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultBiometricManager @Inject constructor(
    context: Context,
    private val repository: BiometricsRepository
) : BiometricsManager {

    override val biometricsManager: BiometricManager by lazy {
        BiometricManager.from(context)
    }
    override val isNoneEnrolledBiometrics: Boolean
        get() = biometricsManager.canAuthenticate(BIOMETRIC_STRONG) ==
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED

    override val currentAccount: CurrentAccount?
        get() = currentAccount

    override fun setCurrentAccount(username: String, password: String) {
        currentAccount?.copy(memberCode = username, password = password)
    }
    override suspend fun canAuthenticateBiometrics(): Flow<Boolean> {
        return combine(
            repository.isBiometricsEnabled(),
            repository.isCmsboEnabled()
        ) { isUserEnabled, isCmsboEnabled ->
            val hasHardwareCapability = checkHardwareCapability()
            isUserEnabled && isCmsboEnabled && hasHardwareCapability
        }
    }

    override suspend fun canDisplayBiometrics(): Flow<Boolean> {
        return combine(
            canAuthenticateBiometrics(),
            repository.getUsernames()
        ) { canAuthenticate, usernames ->
            val hasEnrolledBiometrics = checkHardwareCapability()
            val hasSavedAccounts = usernames != null
            canAuthenticate && hasEnrolledBiometrics && hasSavedAccounts
        }.distinctUntilChanged()
    }

    private fun checkHardwareCapability(): Boolean {
        val authenticators = BIOMETRIC_STRONG // or BIOMETRIC_WEAK
        return biometricsManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }
}