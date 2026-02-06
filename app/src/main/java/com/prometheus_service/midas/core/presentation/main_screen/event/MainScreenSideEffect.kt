package com.prometheus_service.midas.core.presentation.main_screen.event

sealed class MainScreenSideEffect {
    data class RequestGoogleLogin(val url: String) : MainScreenSideEffect()
    object ClearGoogleCredential: MainScreenSideEffect()
}