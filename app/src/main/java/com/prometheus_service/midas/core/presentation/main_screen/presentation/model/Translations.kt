package com.prometheus_service.midas.core.presentation.main_screen.presentation.model


data class ViewTranslations(
    val mainScreenTranslations: MainScreenTranslations,
    val tutorialScreenTranslations: TutorialScreenTranslations,
    val splashScreenTranslations: SplashScreenTranslations,
    val gameScreenTranslations: GameScreenTranslations,
    val biometricsTranslations: BiometricsTranslations,
    val secondStageTranslations: SecondStageTranslations,
    val defaultErrorTranslations: DefaultErrorTranslations
)

data class DefaultErrorTranslations(
    val dialogMessage: String = "",
    val dialogBtn: String = ""
)

data class SecondStageTranslations(
    val pinHeaderCreatePin: String = "",
    val pinHeaderConfirmPin: String = "",
    val pinHeaderEnterPin: String = "",
    val pinHeaderIncorrectPin: String = "",
    val pinHeaderIncorrectPinCreateNew: String = "",
    val pinFooterCancelSettings: String = "",
    val pinFooterRemainingAttempts: String = ""
)

data class BiometricsTranslations(
    val promptTitle: String = "",
    val promptCancel: String = "",

    val dialogSelectAccount: String = "",
    val dialogEnableTitle: String = "",
    val dialogEnableMessage: String = "",
    val dialogNeutralBtnLabel: String = "",
    val dialogPositiveBtnLabel: String = "",
    val dialogNegativeBtnLabel: String = "",

    val currentDialogTitle: String = "",
    val currentDialogMessage: String = "",
    val currentDialogButtonLabel: String = "",

    val biometricToastMessage: String = "",
    val biometricErrorSetupRequired: String = "",
    val biometricErrorCancelled: String = "",
    val biometricErrorDefault: String = "",
    val biometricErrorLockout: String = "",
    val biometricNoneEnrolled: String = ""

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