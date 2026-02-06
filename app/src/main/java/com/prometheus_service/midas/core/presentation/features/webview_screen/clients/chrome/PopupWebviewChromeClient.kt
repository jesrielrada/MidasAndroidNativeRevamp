package com.prometheus_service.midas.core.presentation.features.webview_screen.clients.chrome

import android.app.AlertDialog
import android.content.Context
import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.prometheus_service.midas.core.presentation.features.webview_screen.clients.webview.AnotherPopupWebViewClient
import timber.log.Timber

class PopupWebviewChromeClient(
    private val dialog: AlertDialog,
    private val context: Context
) : WebChromeClient() {

    override fun onCloseWindow(window: WebView?) {
        try {
            window?.destroy()
            dialog.dismiss()
        } catch (e: Exception) {
            Timber.e("Error on closing popup webview ... $e")
        }
    }

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        val anotherPopupWebview = WebView(context).apply {
            webViewClient = AnotherPopupWebViewClient(context)
        }
        (resultMsg?.obj as WebView.WebViewTransport).webView = anotherPopupWebview
        resultMsg.sendToTarget()

        return true
    }
}
