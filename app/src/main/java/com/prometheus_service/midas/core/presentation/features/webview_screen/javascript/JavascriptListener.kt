package com.prometheus_service.midas.core.presentation.features.webview_screen.javascript

interface JavascriptListener {
    fun onPwaReady(data: String)
    fun onNewGameLauncher(url: String)
    fun onPwaNavigate(route: String?)
    fun onNativeAuthenticateGoogle(data: String)
    fun onNativeLaunchGoogle(url: String)
    fun onResetCredentials(data: String)
    fun onShouldDisplayBiometricsLogin(enabled: Boolean)
    fun onLoginLauncher(data: String?)
    fun onPinCodeToggle(isEnabled: Boolean)
    fun onMemberLoggedIn(data: String)
    fun onMemberLoggedOut(data: String)
    fun onSwitchLanguage(language: String)
    fun onOpenInBrowser(url: String)
    fun onLaunchNewWindow(url: String)
    fun onMaintenanceMode(data: String?)
    fun onGeoBlockMode(data: String?)
    fun onRefreshCookie(data: String)
    fun onShouldDisplaySecondStage(isEnabled: Boolean)
}

