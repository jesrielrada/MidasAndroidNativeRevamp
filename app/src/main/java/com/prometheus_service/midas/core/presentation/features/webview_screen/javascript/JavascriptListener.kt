package com.prometheus_service.midas.core.presentation.features.webview_screen.javascript

interface JavascriptListener {
    fun onPwaReady(data: String)
    fun onNewGameLauncher(url: String)
    fun onPwaNavigate(route: String?)
    fun onNativeAuthenticateGoogle(data: String)
    fun onNativeLaunchGoogle(url: String)
    fun onStoreCredentials(data: String?)
    fun onResetCredentials(data: String?)
    fun onShouldDisplayBiometricsLogin(enabled: Boolean)
    fun onLoginLauncher(data: String?)
    fun onPinCodeToggle(isEnabled: Boolean)
    fun onMemberLoggedOut(data: String)
}

