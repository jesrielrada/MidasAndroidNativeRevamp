package com.prometheus_service.midas.core.presentation.features.webview_screen.clients.webview

import android.content.Context
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.net.toUri
import timber.log.Timber

class AnotherPopupWebViewClient(
    private val context: Context
) : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val url = request?.url.toString()

        try {
            view?.destroy()
        } catch (e: Exception) {
            Timber.e(e, "Error destroying another popup webview")
        }

        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)

        return true
    }
}