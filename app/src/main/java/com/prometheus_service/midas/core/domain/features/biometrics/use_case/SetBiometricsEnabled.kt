package com.prometheus_service.midas.core.domain.features.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.repository.BiometricsRepository
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class SetBiometricsEnabled @Inject constructor(
    private val repository: BiometricsRepository,
    private val getMultiLanguageData: GetMultiLanguageData
) {
    suspend fun invoke(locale: String, userEnabled: Boolean) {
        try {
            val translations = getMultiLanguageData.invoke(locale)
            val featureSettings = translations.first().featureSettings
            val isCmsboEnabled = featureSettings.biometricsEnabled
            repository.setBiometricsEnabled(userEnabled, isCmsboEnabled)
        } catch (e: Exception) {
            Timber.e("Setting biometrics enabled failed, $e")
        }
    }
}