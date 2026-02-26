package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.shared.multi_language.MultiLanguageRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CanDisplayMinimumOsDialog @Inject constructor(
    private val appConfigRepository: AppConfigRepository,
    private val multiLanguageRepository: MultiLanguageRepository
) {

    suspend operator fun invoke(
        locale: String,
        currentVersion: String
    ): Boolean {
        val model = multiLanguageRepository.getLocalizedLanguageModel(locale).first()
        val minVersionCode = model.featureSettings.minOsVersionAndroid.toFloat()
        val isCmsboEnabled = model.featureSettings.minOsVersionIsEnabled
        val isToggled = appConfigRepository.appConfigModel.first().isMinimumOsDialogHideToggled

        val versionString = currentVersion.split(".")
            .take(2)
            .joinToString(".")
        val currentVersion = versionString.toFloatOrNull()
        val canDisplay = currentVersion != null && currentVersion > 0f && currentVersion < minVersionCode

        return canDisplay && isCmsboEnabled && isToggled == null
    }
}