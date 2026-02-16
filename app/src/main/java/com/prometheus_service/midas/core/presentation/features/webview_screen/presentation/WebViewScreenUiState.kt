package com.prometheus_service.midas.core.presentation.features.webview_screen.presentation

data class WebViewScreenUiState(
    val isPwaReady: Boolean = false,
    val isUserAgentReady: Boolean = false,
    val isWebViewUrlLoaded: Boolean = false,
    val webviewUrl: String? = null,
    val customUrl: String? = null,
    val customRoute: String? = null,
    val customScript: String? = null,
    val customCallbackScript: String? = null,
    val storeCredentialsData: String? = null,
    val webviewUserAgent: String = "",
    val customUserAgent: String = ""
)