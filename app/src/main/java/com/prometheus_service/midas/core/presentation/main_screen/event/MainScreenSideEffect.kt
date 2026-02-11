package com.prometheus_service.midas.core.presentation.main_screen.event

import javax.crypto.Cipher

sealed class MainScreenSideEffect {
    data class RequestGoogleLogin(val url: String) : MainScreenSideEffect()
    object ClearGoogleCredential : MainScreenSideEffect()
    data class OnPwaReady(val data: String) : MainScreenSideEffect()
    data class OnStoreCredentials(val data: String?) : MainScreenSideEffect()
    data class DisplayBiometricPrompt(
        val cipher: Cipher,
        val isFromAccountSelection: Boolean = false
    ) : MainScreenSideEffect()
    object DisplayBiometricSuccessEnrollment : MainScreenSideEffect()
    data class DisplayBiometricSelectionList(val usernames: List<String>?) : MainScreenSideEffect()
    data class DisplayBiometricAuthError(val code: Int, val message: CharSequence) : MainScreenSideEffect()
}


