package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import kotlinx.coroutines.flow.first
import javax.inject.Inject

sealed class AccountDisplayResult {
    data class AccountDisplayList(val usernames: List<String>?) : AccountDisplayResult()
    object DisplayNoneEnrolled : AccountDisplayResult()
}

class HandleBiometricAccountDisplay @Inject constructor(
    private val biometricsManager: BiometricsManager,
    private val setBiometricsEnabled: SetBiometricsEnabled
) {

    suspend operator fun invoke(locale: String): Result<AccountDisplayResult> {
        try {
            val canDisplayAccounts = biometricsManager.canDisplayBiometrics().first()
            if (canDisplayAccounts) {
                val usernames = biometricsManager.getUsernames()
                return Result.success(AccountDisplayResult.AccountDisplayList(usernames))
            } else if (biometricsManager.isNoneEnrolledBiometrics) {
                biometricsManager.setCurrentAccount(CurrentAccount()) //to clear account
                biometricsManager.deleteAllAccounts()
                setBiometricsEnabled.invoke(locale, false)
                return Result.success(AccountDisplayResult.DisplayNoneEnrolled)
            } else {
                biometricsManager.setCurrentAccount(CurrentAccount()) //to clear account
                biometricsManager.deleteAllAccounts()
                setBiometricsEnabled.invoke(locale, false)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
        return Result.failure(Exception("Biometrics not enabled"))
    }
}