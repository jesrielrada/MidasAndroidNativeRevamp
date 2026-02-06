package com.prometheus_service.midas.core.presentation.features.webview_screen.presentation

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.prometheus_service.midas.core.presentation.features.webview_screen.clients.chrome.DefaultWebChromeClient
import com.prometheus_service.midas.core.presentation.features.webview_screen.clients.webview.DefaultWebviewClient
import com.prometheus_service.midas.core.presentation.features.webview_screen.javascript.DefaultJavascriptListener
import com.prometheus_service.midas.core.presentation.features.webview_screen.javascript.JavascriptListener
import timber.log.Timber

@Composable
fun WebviewScreen(
    modifier: Modifier = Modifier,
    uiState: WebViewScreenUiState,
    onWebviewInitialized: (String) -> Unit,
    onPwaReady: (String) -> Unit,
    onNewGameLauncher: (String) -> Unit,
    onRouteLoaded: () -> Unit,
    onUrlLoaded: () -> Unit
) {
    WebviewScreenContent(
        modifier = modifier,
        uiState = uiState,
        onInitialized = { onWebviewInitialized(it) },
        onPwaReady = { onPwaReady(it) },
        onNewGameLauncher = { onNewGameLauncher(it) },
        onRouteLoaded = { onRouteLoaded() },
        onUrlLoaded = onUrlLoaded
    )
}


@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun WebviewScreenContent(
    modifier: Modifier = Modifier,
    uiState: WebViewScreenUiState,
    onInitialized: (userAgent: String) -> Unit,
    onPwaReady: (String) -> Unit,
    onNewGameLauncher: (String) -> Unit,
    onUrlLoaded: () -> Unit,
    onRouteLoaded: () -> Unit
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.overScrollMode = View.OVER_SCROLL_NEVER
            this.settings.javaScriptEnabled = true
            this.settings.domStorageEnabled = true
            this.settings.allowFileAccess = true
            this.settings.allowContentAccess = true
            this.settings.javaScriptCanOpenWindowsAutomatically = true
            this.settings.setSupportMultipleWindows(true)

            webviewJavascriptSetup(
                webView = this,
                onPwaReady = { onPwaReady(it) },
                onNewGameLauncher = { onNewGameLauncher(it) }
            )

            this.webViewClient = DefaultWebviewClient()
            this.webChromeClient = DefaultWebChromeClient(context)

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            onInitialized(this.settings.userAgentString)
        }
    }

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    Scaffold(modifier = modifier) { innerPadding ->
        AndroidView(
            factory = {
                webView
            },
            update = { view ->
                val targetUrl = uiState.webviewUrl
                if (targetUrl != null && !uiState.isWebViewUrlLoaded) {
                    Timber.d("Loading webview url ... $targetUrl")
                    view.loadUrl(targetUrl)
                    onUrlLoaded()
                }

                val targetRoute = uiState.customRoute
                if (targetRoute != null) {
                    Timber.d("Loading custom route ... $targetRoute")
                    view.loadUrl(targetRoute)
                    onRouteLoaded()
                }
            },
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

fun webviewJavascriptSetup(
    webView: WebView,
    onPwaReady: (data: String) -> Unit,
    onNewGameLauncher: (url: String) -> Unit
) {
    webView.addJavascriptInterface(
        DefaultJavascriptListener(
            object : JavascriptListener {
                override fun onPwaReady(data: String) {
                    onPwaReady(data)
                }

                override fun onNewGameLauncher(url: String) {
                    onNewGameLauncher(url)
                }
            }
        ),
        "Android"
    )
}