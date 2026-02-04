package com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case

import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CanDisplayTutorial @Inject constructor(
    private val getSplashTutorialData: GetSplashTutorialData,
    private val getAppConfigModel: GetAppConfigModel
) {
    suspend operator fun invoke(): Boolean {
        val splashData = getSplashTutorialData.invoke().first()
        val appConfig = getAppConfigModel.invoke().first()

        val images = splashData.tutorialImages
        val isEnabled = splashData.isTutorialScreenEnabled
        val isTutorialDisplayed = appConfig.isTutorialDisplayed
        val isLanguageSelectionDisplayed = appConfig.isLanguageSelectionDisplayed

        return images.isNotEmpty() && isEnabled && isTutorialDisplayed == null && isLanguageSelectionDisplayed != null
    }
}