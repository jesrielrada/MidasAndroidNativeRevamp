package com.prometheus_service.midas.core.presentation.features.webview_screen.presentation

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
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
    onRouteLoaded: () -> Unit,
    onUrlLoaded: () -> Unit,
    onCustomUrlLoaded: () -> Unit,
    onPwaReady: (String) -> Unit,
    onPwaNavigate: (String?) -> Unit,
    onStoreCredentials: (String?) -> Unit,
    onNewGameLauncher: (String) -> Unit,
    onNativeAuthenticateGoogle: (String) -> Unit,
    onNativeLaunchGoogle: (String) -> Unit
) {
    WebviewScreenContent(
        modifier = modifier,
        uiState = uiState,
        onInitialized = { onWebviewInitialized(it) },
        onPwaReady = { onPwaReady(it) },
        onPwaNavigate = { onPwaNavigate(it) },
        onStoreCredentials = { onStoreCredentials(it) },
        onNewGameLauncher = { onNewGameLauncher(it) },
        onRouteLoaded = { onRouteLoaded() },
        onUrlLoaded = onUrlLoaded,
        onNativeAuthenticateGoogle = { onNativeAuthenticateGoogle(it) },
        onNativeLaunchGoogle = { onNativeLaunchGoogle(it) },
        onCustomUrlLoaded = onCustomUrlLoaded
    )
}


@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun WebviewScreenContent(
    modifier: Modifier = Modifier,
    uiState: WebViewScreenUiState,
    onInitialized: (userAgent: String) -> Unit,
    onUrlLoaded: () -> Unit,
    onRouteLoaded: () -> Unit,
    onCustomUrlLoaded: () -> Unit,
    onPwaReady: (String) -> Unit,
    onPwaNavigate: (String?) -> Unit,
    onStoreCredentials: (String?) -> Unit,
    onNewGameLauncher: (String) -> Unit,
    onNativeAuthenticateGoogle: (String) -> Unit,
    onNativeLaunchGoogle: (String) -> Unit
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
                onNewGameLauncher = { onNewGameLauncher(it) },
                onNativeAuthenticateGoogle = { onNativeAuthenticateGoogle(it) },
                onNativeLaunchGoogle = { onNativeLaunchGoogle(it) },
                onStoreCredentials = { onStoreCredentials(it) },
                onPwaNavigate = { onPwaNavigate(it) }
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

                if (uiState.customUrl != null) {
                    Timber.d("Loading custom url ... ${uiState.customUrl}")
                    view.loadUrl(uiState.customUrl)
                    onCustomUrlLoaded()
                }

                if(uiState.customScript != null) {
                    Timber.d("Loading custom script ... ${uiState.customScript}")
                    view.loadUrl(uiState.customScript)
                    onCustomUrlLoaded()
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
    onNewGameLauncher: (url: String) -> Unit,
    onNativeAuthenticateGoogle: (data: String) -> Unit,
    onNativeLaunchGoogle: (url: String) -> Unit,
    onStoreCredentials: (data: String?) -> Unit,
    onPwaNavigate: (route: String?) -> Unit
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

                override fun onPwaNavigate(route: String?) {
                    onPwaNavigate(route)
                }

                override fun onNativeAuthenticateGoogle(data: String) {
                    onNativeAuthenticateGoogle(data)
                }

                override fun onNativeLaunchGoogle(url: String) {
                    onNativeLaunchGoogle(url)
                }

                override fun onStoreCredentials(data: String?) {
                    onStoreCredentials(data)
                }

                override fun onResetCredentials(data: String?) {
                    TODO("Not yet implemented")
                }

                override fun onShouldDisplayBiometricsLogin(enabled: Boolean) {
                    TODO("Not yet implemented")
                }
            }
        ),
        "Android"
    )
}