package com.prometheus_service.midas.core.presentation.features.game_screen.presentation.components.webview

import android.annotation.SuppressLint
import android.net.http.SslError
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import timber.log.Timber


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GameWebview(
    modifier: Modifier = Modifier,
    gameUrl: String? = null,
    onGameLoaded: () -> Unit
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.domStorageEnabled = true
            settings.useWideViewPort = true //set view into full screen
            settings.loadWithOverviewMode = true
            scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY

            gameWebviewClientSetup(this)
            gameWebChromeClientSetup(this, onGameLoaded)

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        }
    }

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    AndroidView(
        factory = {
            webView
        },
        update = { view ->
            val targetUrl = gameUrl
            if (gameUrl != null && targetUrl != view.url) {
                Timber.d("Loading game url... $targetUrl")
                view.loadUrl(targetUrl)
            }
        },
        modifier = modifier.fillMaxSize()
    )
}



fun gameWebviewClientSetup(webView: WebView) {
    webView.webViewClient = object : WebViewClient() {
        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            Timber.d("onReceivedSslError called.. proceeding")
            handler?.proceed()
        }
    }
}

fun gameWebChromeClientSetup(
    webView: WebView,
    onGameLoaded: () -> Unit
) {
    webView.webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                onGameLoaded()
            }
        }
    }
}