package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import javax.inject.Inject

class HandleBiometricAuthError @Inject constructor(
    private val biometricsManager: BiometricsManager,
    private val setBiometricsEnabled: SetBiometricsEnabled,
) {
    companion object {
        private const val CHANGE_PASSWORD_ROUTE = "change-password-route"
        private const val SECURITY_ROUTE = "security-route"
    }

    suspend operator fun invoke(currentRoute: String?, locale: String) {
        val username = biometricsManager.currentAccount?.memberCode
        val isUpdateRoute = currentRoute.equals(SECURITY_ROUTE) ||
                currentRoute.equals(CHANGE_PASSWORD_ROUTE)

        if (isUpdateRoute && username != null) {
            biometricsManager.deleteAccount(username)
            setBiometricsEnabled.invoke(locale, false)
        }
    }

}