package com.prometheus_service.midas.core.presentation.features.webview_screen.clients.chrome

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Message
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.prometheus_service.midas.core.presentation.features.webview_screen.clients.webview.PopupWebviewClient

class DefaultWebChromeClient(
    private val context: Context
) : WebChromeClient() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        val popupWebview = WebView(context).apply {
            this.settings.javaScriptEnabled = true
            this.settings.setSupportMultipleWindows(true)
            this.settings.javaScriptCanOpenWindowsAutomatically = true

            this.webViewClient = PopupWebviewClient()

            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        }

        val dialog = AlertDialog.Builder(context).create().apply {
            this.setTitle("")
            this.setView(popupWebview)
            this.show()
            this.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
            )
            this.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.setCancelable(false)
            this.setCanceledOnTouchOutside(false)
        }

        popupWebview.webChromeClient = PopupWebviewChromeClient(dialog = dialog, context = context)

        val transport = resultMsg?.obj as WebView.WebViewTransport
        transport.webView = popupWebview
        resultMsg.sendToTarget()

        return true
    }
}


