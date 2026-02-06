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
}