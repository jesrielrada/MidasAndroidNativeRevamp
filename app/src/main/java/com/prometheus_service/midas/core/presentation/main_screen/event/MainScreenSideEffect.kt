package com.prometheus_service.midas.core.presentation.main_screen.event

import com.prometheus_service.midas.cmspwaupdater.VersionInfo
import javax.crypto.Cipher

sealed class MainScreenSideEffect {
    data class RequestGoogleLogin(val url: String) : MainScreenSideEffect()
    object ClearGoogleCredential : MainScreenSideEffect()
    data class OnPwaReady(val data: String) : MainScreenSideEffect()
    data class DisplayBiometricPrompt(
        val cipher: Cipher,
        val isFromAccountSelection: Boolean = false
    ) : MainScreenSideEffect()
    object DisplayBiometricSuccessEnrollment : MainScreenSideEffect()
    data class DisplayBiometricSelectionList(val usernames: List<String>?) : MainScreenSideEffect()
    data class DisplayBiometricAuthError(val code: Int, val message: CharSequence) : MainScreenSideEffect()
    data class StartActionView(val url: String) : MainScreenSideEffect()
    data class LaunchUpdateActivity(val versionInfo: VersionInfo) : MainScreenSideEffect()
    data class LaunchRequestPermission(val versionInfo: VersionInfo) : MainScreenSideEffect()
}


