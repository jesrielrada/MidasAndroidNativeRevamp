package com.prometheus_service.midas.core.presentation.features.webview_screen.javascript

import android.webkit.JavascriptInterface
import androidx.annotation.Keep

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
    fun memberLoggedOut(data: String) {
        callback.onMemberLoggedOut(data)
    }

}