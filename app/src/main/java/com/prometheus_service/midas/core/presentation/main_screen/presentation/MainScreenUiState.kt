package com.prometheus_service.midas.core.presentation.main_screen.presentation

import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.WebViewScreenUiState

/**
 * Initialize setup is splash, tutorial, language selection, and webview should be set to true
 * It will hide on its own when the condition is met
 */
data class MainScreenUiState(
    val shouldDisplaySplash: Boolean = true,
    val shouldDisplayTutorial: Boolean = false,
    val shouldDisplayLanguageSelection: Boolean = false,
    val shouldDisplayWebview: Boolean = true,
    val shouldDisplayGameView: Boolean = false,
    val canDisplayTutorialScreen: Boolean = false,
    val isAppInitialized: Boolean = false,
    val isNetworkReady: Boolean = false,
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
        biometricsTranslations = BiometricsTranslations()
    )
)

data class ViewTranslations(
    val mainScreenTranslations: MainScreenTranslations,
    val tutorialScreenTranslations: TutorialScreenTranslations,
    val splashScreenTranslations: SplashScreenTranslations,
    val gameScreenTranslations: GameScreenTranslations,
    val biometricsTranslations: BiometricsTranslations
)

data class BiometricsTranslations(
    val biometricsDialogTitle: String = "",
    val biometricsDialogMessage: String = "",
    val biometricsDialogButtonLabel: String = "",
)
data class SplashScreenTranslations(
    val skipLabel: String = ""
)

data class TutorialScreenTranslations(
    val buttonDefaultLabel: String = "",
    val buttonEndLabel: String = ""
)

data class MainScreenTranslations(
    val initializeErrorMessage: String = "",
    val retryButtonLabel: String = ""
)

data class GameScreenTranslations(
    val returnDialogMessage: String = "",
    val returnDialogConfirm: String = "",
    val returnDialogCancel: String = ""
)