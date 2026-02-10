package com.prometheus_service.midas.core.domain.shared.biometrics.use_case

import com.prometheus_service.midas.core.domain.features.biometrics.repository.BiometricsRepository
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class SetBiometricsEnabled @Inject constructor(
    private val repository: BiometricsRepository,
    private val getMultiLanguageData: GetMultiLanguageData
) {
    suspend fun invoke(locale: String) {
        try {
            val translations = getMultiLanguageData.invoke(locale)

            Timber.d("Getting translations for locale $locale, $translations")

            val featureSettings = translations.first().featureSettings
            val isCmsboEnabled = featureSettings.biometricsEnabled

            Timber.d("Setting biometrics enabled to $isCmsboEnabled")

            repository.setBiometricsEnabled(true, isCmsboEnabled)
        } catch (e: Exception) {
            Timber.e("Setting biometrics enabled failed, $e")
        }

    }
}