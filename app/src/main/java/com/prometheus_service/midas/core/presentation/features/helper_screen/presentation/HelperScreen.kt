package com.prometheus_service.midas.core.presentation.features.helper_screen.presentation

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.prometheus_service.midas.R
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.webviewDownloadInitializer
import com.prometheus_service.midas.shared.theme.MidasAndroidNativeRevampTheme
import timber.log.Timber

@Composable
fun HelperScreen(
    modifier: Modifier = Modifier,
    uiState: HelperUiState,
    onHideScreen: () -> Unit,
    onDownloadProcessed: (String) -> Unit
) {

    HelperScreenContent(
        modifier = modifier,
        uiState = uiState,
        hideScreen = onHideScreen,
        onDownloadProcessed = onDownloadProcessed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HelperScreenContent(
    modifier: Modifier,
    uiState: HelperUiState,
    hideScreen: () -> Unit,
    onDownloadProcessed: (String) -> Unit
) {
    val context = LocalContext.current
    val downloadManager = remember { context.getSystemService(DownloadManager::class.java) }

    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            isScrollbarFadingEnabled = false
            scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY

            this.webViewClient = WebViewClient()

            webviewDownloadInitializer(
                view = this,
                downloadManager = downloadManager,
                onDownloadProcessed = onDownloadProcessed
            )

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
        }
    }

    LaunchedEffect(uiState.displayMessage) {
        if(uiState.displayMessage != null) {
            Toast.makeText(context, uiState.displayMessage, Toast.LENGTH_LONG).show()
            hideScreen()
        }
    }

    LaunchedEffect(uiState.url) {
        if (uiState.url != null) {
            Timber.d("Loading helper screen url: ${uiState.url}")
            webView.loadUrl(uiState.url)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Timber.d("Disposing helper screen webview")
            webView.destroy()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Your Center Image/Logo
                    Image(
                        painter = painterResource(id = R.drawable.brand_logo_2),
                        contentDescription = null,
                        modifier = Modifier.height(30.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (webView.canGoBack()) {
                            webView.goBack()
                        } else {
                            hideScreen()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0XFFF6C244)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0XFF272D34) // Set your preferred color
                )
            )
        }
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            factory = { webView }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HelperScreenPreview() {
    MidasAndroidNativeRevampTheme {
        HelperScreenContent(
            modifier = Modifier,
            uiState = HelperUiState(),
            hideScreen = {},
            onDownloadProcessed = {}
        )
    }
}



