package com.prometheus_service.midas.core.presentation.features.webview_screen.presentation

data class WebViewScreenUiState(
    val isWebviewReady: Boolean = false,
    val isUserAgentReady: Boolean = false,
    val webviewUrl: String = "",
    val webviewUserAgent: String = "",
    val customUserAgent: String = ""
)