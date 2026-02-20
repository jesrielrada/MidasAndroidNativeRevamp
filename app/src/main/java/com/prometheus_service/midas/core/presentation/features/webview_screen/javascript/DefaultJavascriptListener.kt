package com.prometheus_service.midas.core.presentation.features.webview_screen.javascript

import android.webkit.JavascriptInterface
import androidx.annotation.Keep
import androidx.annotation.Nullable
import timber.log.Timber

@Keep
class DefaultJavascriptListener(val callback: JavascriptListener) {
    @JavascriptInterface
    fun pwaReady(data: String) {
        callback.onPwaReady(data)
    }

    @JavascriptInterface
    fun newGameLauncher(url: String) {
        callback.onNewGameLauncher(url)
    }

    @JavascriptInterface
    fun nativeAuthenticateGoogle(data: String) {
        callback.onNativeAuthenticateGoogle(data)
    }

    @JavascriptInterface
    fun nativeLaunchGoogle(url: String) {
        callback.onNativeLaunchGoogle(url)
    }

    @JavascriptInterface
    fun storeCredentials(data: String?) {
        callback.onStoreCredentials(data)
    }

    @JavascriptInterface
    fun resetCredentials(data: String?) {
        callback.onResetCredentials(data)
    }

    @JavascriptInterface
    fun shouldDisplayBiometricsLogin(enabled: Boolean) {
        callback.onShouldDisplayBiometricsLogin(enabled)
    }

    @JavascriptInterface
    fun pwaNavigateTo(data: String?) {
        callback.onPwaNavigate(data)
    }

    @JavascriptInterface
    fun loginLauncher(data: String?) {
        callback.onLoginLauncher(data)
    }

    @JavascriptInterface
    fun pincodeToggled(isEnabled: Boolean) {
        callback.onPinCodeToggle(isEnabled)
    }

    @JavascriptInterface
    fun memberLoggedIn(data: String) {
        callback.onMemberLoggedIn(data)
    }

    @JavascriptInterface
    fun memberLoggedOut(data: String) {
        callback.onMemberLoggedOut(data)
    }

    @JavascriptInterface
    fun switchLanguage(language: String) {
        callback.onSwitchLanguage(language)
    }

    @JavascriptInterface
    fun openInBrowser(url: String) {
        callback.onOpenInBrowser(url)
    }

    @JavascriptInterface
    fun launchNewWindow(url: String) {
        callback.onLaunchNewWindow(url)
    }

    @JavascriptInterface
    fun maintenanceMode(data: String?) {
        callback.onMaintenanceMode(data)
    }

    @JavascriptInterface
    fun geoBlockMode(data: String?) {
        callback.onGeoBlockMode(data)
    }

    @JavascriptInterface
    fun themeSetting(data: String) {
        Timber.d("JavascriptListener: themeSetting: $data")
        callback.onThemeSetting(data)
    }

    @JavascriptInterface
    fun refreshCookie(data: String) {
        Timber.d("JavascriptListener: refreshCookie: $data")
        callback.onRefreshCookie(data)
    }

}