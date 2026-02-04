package com.prometheus_service.midas.core.presentation.main_screen.presentation

data class MainScreenUiState(
    val shouldDisplaySplash: Boolean = false,
    val shouldDisplayTutorial: Boolean = false,
    val shouldDisplayLanguageSelection: Boolean = false,
    val shouldDisplayWebview: Boolean = false,
    val shouldDisplayGameView: Boolean = true,
    val isWebviewReady: Boolean = false,
    val isAppInitialized: Boolean = false,
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