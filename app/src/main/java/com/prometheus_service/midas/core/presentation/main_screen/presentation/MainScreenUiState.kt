package com.prometheus_service.midas.core.presentation.main_screen.presentation

import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.WebViewScreenUiState
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.BiometricsTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.GameScreenTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.MainScreenTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.SecondStageTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.SplashScreenTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.TutorialScreenTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.ViewTranslations


data class MainScreenUiState(
    val shouldDisplaySplash: Boolean = true,
    val shouldDisplayTutorial: Boolean = false,
    val shouldDisplayLanguageSelection: Boolean = false,
    val shouldDisplayWebview: Boolean = true,
    val shouldDisplayGameView: Boolean = false,
    val shouldDisplaySecondStage: Boolean = false,
    val canDisplayTutorialScreen: Boolean = false,
    val isAppInitialized: Boolean = false,
    val isNetworkReady: Boolean = false,
    val onDataSync: Boolean = false,
    val networkType: String = "",
    val currentLocale: String = "",
    val currentRoute: String = "",
    val biometricCurrentAccount: CurrentAccount? = null,
    val isErrorDialogVisible: Boolean = false,
    val isBiometricsLoadingDialogVisible: Boolean = false,
    val isBiometricsEnableDialogVisible: Boolean = false,
    val isBiometricsErrorDialogVisible: Boolean = false,
    val gameUrl: String? = null,
    val webViewScreenUiState: WebViewScreenUiState = WebViewScreenUiState(),
    val viewTranslations: ViewTranslations = ViewTranslations(
        mainScreenTranslations = MainScreenTranslations(),
        tutorialScreenTranslations = TutorialScreenTranslations(),
        splashScreenTranslations = SplashScreenTranslations(),
        gameScreenTranslations = GameScreenTranslations(),
        biometricsTranslations = BiometricsTranslations(),
        secondStageTranslations = SecondStageTranslations()
    )
)

