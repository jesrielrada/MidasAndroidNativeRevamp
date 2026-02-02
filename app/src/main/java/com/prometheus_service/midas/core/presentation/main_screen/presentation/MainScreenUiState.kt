package com.prometheus_service.midas.core.presentation.main_screen.presentation

data class MainScreenUiState(
    val shouldDisplaySplash: Boolean = true,
    val shouldDisplayTutorial: Boolean = true,
    val shouldDisplayLanguageSelection: Boolean = true,
    val shouldDisplayWebview: Boolean = false,
    val isWebviewReady: Boolean = false,
    val isAppInitialized: Boolean = false,
    val isErrorDialogVisible: Boolean = false,
    val viewTranslations: ViewTranslations = ViewTranslations(
        mainScreenTranslations = MainScreenTranslations(),
        tutorialScreenTranslations = TutorialScreenTranslations(),
        splashScreenTranslations = SplashScreenTranslations()
    )
)

data class ViewTranslations(
    val mainScreenTranslations: MainScreenTranslations,
    val tutorialScreenTranslations: TutorialScreenTranslations,
    val splashScreenTranslations: SplashScreenTranslations
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