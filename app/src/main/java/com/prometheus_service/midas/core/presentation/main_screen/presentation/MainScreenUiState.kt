package com.prometheus_service.midas.core.presentation.main_screen.presentation

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
    val isWebviewReady: Boolean = false,
    val isAppInitialized: Boolean = false,
    val isUserAgentReady: Boolean = false,
    val isNetworkReady: Boolean = false,
    val networkType: String = "",
    val webViewUserAgent: String = "",
    val customUserAgent: String = "",
    val webviewUrl: String = "",
    val currentLocale: String = "",
    val isErrorDialogVisible: Boolean = false,
    val viewTranslations: ViewTranslations = ViewTranslations(
        mainScreenTranslations = MainScreenTranslations(),
        tutorialScreenTranslations = TutorialScreenTranslations(),
        splashScreenTranslations = SplashScreenTranslations(),
        gameScreenTranslations = GameScreenTranslations()
    )
)

data class ViewTranslations(
    val mainScreenTranslations: MainScreenTranslations,
    val tutorialScreenTranslations: TutorialScreenTranslations,
    val splashScreenTranslations: SplashScreenTranslations,
    val gameScreenTranslations: GameScreenTranslations
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