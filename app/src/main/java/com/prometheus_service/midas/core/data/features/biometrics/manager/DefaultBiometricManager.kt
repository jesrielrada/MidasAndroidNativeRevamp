package com.prometheus_service.midas.core.data.features.biometrics.manager

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CipherTextWrapper
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import com.prometheus_service.midas.core.domain.features.biometrics.repository.BiometricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import timber.log.Timber
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
    private var _currentAccount: CurrentAccount? = null
    override val currentAccount: CurrentAccount?
        get() = _currentAccount

    private var _cipherTextWrapper: CipherTextWrapper? = null

    override val cipherTextWrapper: CipherTextWrapper?
        get() = _cipherTextWrapper

    override suspend fun setCipherTextWrapper(cipherTextWrapper: CipherTextWrapper) {
        _cipherTextWrapper = cipherTextWrapper
    }

    override suspend fun getEncryptedPassword(username: String): CipherTextWrapper? {
        return repository.getCipherTextWrapper(username).first()
    }

    override suspend fun getUsernames(): List<String>? {
        return repository.getUsernames().first()
    }

    override suspend fun isBiometricsEnabled(): Boolean {
        return repository.isBiometricsEnabled().first()
    }

    override suspend fun setBiometricsEnabled(cmsboEnabled: Boolean) {
        return repository.setBiometricsEnabled(true, cmsboEnabled)
    }

    override suspend fun setBiometricsCmsboEnabled(cmsboEnabled: Boolean) {
        repository.setBiometricsCmsboEnabled(cmsboEnabled)
    }

    override suspend fun setCurrentAccount(data: String): Result<Unit> {
        return runCatching {
            val account = requireNotNull(repository.parseRemoteData(data)) { "Account is null" }
            require(!account.bio.isNullOrEmpty()) { "Bio/Password is null" }
            require(!account.memberCode.isNullOrEmpty()) { "Member code is null" }
            Timber.d("Setting current account: $account")
            _currentAccount = account
        }.onFailure {
            Timber.d("Setting current account failed: $it")
        }
    }

    override suspend fun setCurrentAccount(currentAccount: CurrentAccount) {
        _currentAccount = if (_currentAccount == null) {
            // If nothing exists yet, just use the provided account
            currentAccount
        } else {
            // If it exists, merge the values: use new value if not null, otherwise keep old
            _currentAccount?.copy(
                memberCode = currentAccount.memberCode ?: _currentAccount?.memberCode,
                bio = currentAccount.bio ?: _currentAccount?.bio
            )
        }
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

    override suspend fun canEnrollBiometrics(): Boolean {
        return checkHardwareCapability()
    }

    override suspend fun isUserEnrolled(username: String): Boolean {
        return repository.doesUserExists(username).first()
    }

    override suspend fun persistAccount(
        username: String,
        encryptedPassword: CipherTextWrapper?
    ): Result<Unit> {
        return runCatching {
            repository.persistCipherTextWrapper(
                cipherTextWrapper = encryptedPassword!!,
                memberCode = username
            )
            repository.persistUsername(username)
            Timber.d("Success persisting account ... ")
        }.onFailure {
            Timber.d("Persisting account failed: $it")
        }
    }

    override suspend fun deleteAccount(username: String) {
        repository.deleteAccount(username)
    }

    override suspend fun deleteAllAccounts() {
        repository.deleteAccountsList()
    }

    private fun checkHardwareCapability(): Boolean {
        val authenticators = BIOMETRIC_STRONG
        return biometricsManager.canAuthenticate(authenticators) == BiometricManager.BIOMETRIC_SUCCESS
    }
}