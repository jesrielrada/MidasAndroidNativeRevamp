package com.prometheus_service.midas.core.presentation.main_screen.event

import androidx.biometric.BiometricPrompt
import androidx.credentials.GetCredentialResponse

sealed class MainScreenEvent {
    object HideSplashScreen : MainScreenEvent()
    object HideTutorialScreen : MainScreenEvent()
    object HideLanguageSelectionScreen : MainScreenEvent()
    object HideGameViewScreen : MainScreenEvent()
    object InitializeApplication : MainScreenEvent()
    object InitializeTranslations : MainScreenEvent()
    object InitializeTutorialSettings : MainScreenEvent()
    object DismissErrorDialog : MainScreenEvent()
    object InitializeNetworkType : MainScreenEvent()
    object BuildUserAgent : MainScreenEvent()
    data class SetUserAgentReady(val userAgent: String) : MainScreenEvent()
    object InitializeLocale : MainScreenEvent()
    object DisplayLanguageSelectionScreen : MainScreenEvent()
    data class SetLocaleSelected(val locale: String) : MainScreenEvent()
    object DisplayTutorialScreen : MainScreenEvent()
    object LoadBaseUrl : MainScreenEvent()
    data class LaunchGamePage(val gamePath: String) : MainScreenEvent()
    data class LoadCustomRoute(val route: String) : MainScreenEvent()
    data class LoadCustomScript(val script: String) : MainScreenEvent()
    object ResetCustomRoute : MainScreenEvent()
    object SetWebviewUrlLoaded : MainScreenEvent()
    data class HandlePwaReady(val data: String) : MainScreenEvent()
    data class HandleStoreCredentials(val data: String?) : MainScreenEvent()
    data class ProcessGoogleLogin(
        val clientId: String,
        val url: String,
        val response: GetCredentialResponse
    ) : MainScreenEvent()

    data class LoadCustomUrl(val customUrl: String) : MainScreenEvent()
    object ResetCustomUrl : MainScreenEvent()
    data class UpdateCurrentRoute(val route: String) : MainScreenEvent()
    object InitializeBiometricPrompt : MainScreenEvent()
    data class HandleBiometricsAuthResult(val result: BiometricPrompt.AuthenticationResult) : MainScreenEvent()
    object DisplayBiometricAccountSelection : MainScreenEvent()
    data class HandleAccountSelected(val username: String) : MainScreenEvent()
    data class HandleAccountSelectedAuthSucceed(val result: BiometricPrompt.AuthenticationResult) : MainScreenEvent()
    object HandleAccountSelectionAuthCancelled : MainScreenEvent()
    object HandleBiometricsLogin : MainScreenEvent()
    object SetBiometricsDisabled : MainScreenEvent()
    object HideBiometricEnableDialog : MainScreenEvent()
    object HideBiometricErrorDialog : MainScreenEvent()
    data class HandleBiometricsAuthError(
        val code: Int,
        val message: CharSequence
    ) : MainScreenEvent()
}