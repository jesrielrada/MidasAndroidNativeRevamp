package com.prometheus_service.midas.core.presentation.features.webview_screen.clients.webview

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.prometheus_service.midas.core.presentation.features.webview_screen.strategies.redirectOverrideStrategies
import timber.log.Timber

class DefaultWebviewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Timber.d("Should override url loading ... ${request?.url}")

        val url = request?.url.toString().removeSuffix("/")
        val isRedirect = request?.isRedirect == true

        if (isRedirect && redirectOverrideStrategies.any { it.matches(url) }) {
            Timber.d("Redirecting to ...  $url")
            return true
        }

        return super.shouldOverrideUrlLoading(view, request)
    }
}