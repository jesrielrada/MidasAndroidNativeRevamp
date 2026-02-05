package com.prometheus_service.midas.core.presentation.features.webview_screen.javascript

interface JavascriptListener {
    fun onPwaReady(data: String)
    fun onNewGameLauncher(url: String)
}

