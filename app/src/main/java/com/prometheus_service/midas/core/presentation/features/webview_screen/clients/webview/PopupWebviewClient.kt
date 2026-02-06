package com.prometheus_service.midas.core.presentation.features.webview_screen.clients.webview

import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.RenderProcessGoneDetail
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import timber.log.Timber

class PopupWebviewClient : WebViewClient() {
    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        Timber.d("Received ssl error on popup webview ... ")
        handler?.proceed()
    }

    override fun onRenderProcessGone(
        view: WebView?,
        detail: RenderProcessGoneDetail?
    ): Boolean {
        Timber.d("Render process gone on popup webview ...")
        return super.onRenderProcessGone(view, detail)
    }
}