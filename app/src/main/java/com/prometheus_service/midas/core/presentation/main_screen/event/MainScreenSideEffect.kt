package com.prometheus_service.midas.core.presentation.main_screen.event

sealed class MainScreenSideEffect {
    data class RequestGoogleLogin(val url: String) : MainScreenSideEffect()
    object ClearGoogleCredential: MainScreenSideEffect()

    data class OnPwaReady(val data: String) : MainScreenSideEffect()

    data class OnStoreCredentials(val data: String?) : MainScreenSideEffect()
}