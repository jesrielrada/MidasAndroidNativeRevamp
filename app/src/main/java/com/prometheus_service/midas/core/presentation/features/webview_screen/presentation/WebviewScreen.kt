package com.prometheus_service.midas.core.presentation.features.webview_screen.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import timber.log.Timber

@Composable
fun WebviewScreen(
    modifier: Modifier = Modifier,
    viewModel: WebViewScreenViewModel = hiltViewModel(),
    onPageFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WebviewScreenContent(
        modifier = modifier,
        uiState = uiState,
        onPageFinished = onPageFinished
    )
}


@Composable
fun WebviewClientSetup(
    webView: WebView,
    onPageFinished: () -> Unit
) {
    LaunchedEffect(webView) {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Timber.d("onPageStarted: $url")
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Timber.d("onPageFinished: $url")
                onPageFinished()
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebviewScreenContent(
    modifier: Modifier = Modifier,
    uiState: WebViewScreenUiState,
    onPageFinished: () -> Unit
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

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        }
    }

    WebviewClientSetup(
        webView = webView,
        onPageFinished = onPageFinished
    )

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
                val targetUrl = uiState.url ?: "https://epm.vn88uat.com"
                if (view.url != targetUrl) {
                    Timber.d("Loading url... $targetUrl")
                    view.loadUrl(targetUrl)
                }
            },
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}