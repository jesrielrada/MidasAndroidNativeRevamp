package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import kotlinx.coroutines.flow.first
import javax.inject.Inject


sealed class DisplayResult {
    data class DisplayList(val usernames: List<String>?) : DisplayResult()
    object DisplayNoneEnrolled : DisplayResult()
}


class HandleBiometricAccountDisplay @Inject constructor(
    private val biometricsManager: BiometricsManager
) {

    suspend operator fun invoke(): Result<DisplayResult> {
        val canDisplayAccounts = biometricsManager.canDisplayBiometrics().first()
        if (canDisplayAccounts) {
            val usernames = biometricsManager.getUsernames()
            return Result.success(DisplayResult.DisplayList(usernames))
        } else if (biometricsManager.isNoneEnrolledBiometrics) {
            return Result.success(DisplayResult.DisplayNoneEnrolled)
        }

        return Result.failure(Exception("Biometrics not enabled"))
    }
}