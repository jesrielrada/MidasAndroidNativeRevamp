package com.prometheus_service.midas.core.presentation.features.webview_screen.strategies

class NonAuthCallbackStrategy : RedirectOverrideStrategy {
    override fun matches(url: String): Boolean {
        return url.startsWith("https://", ignoreCase = true) && url.contains("tauth-callback").not()
    }
}