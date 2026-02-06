package com.prometheus_service.midas.core.presentation.features.webview_screen.strategies

class NonHttpsStrategy : RedirectOverrideStrategy {
    override fun matches(url: String): Boolean {
        return url.startsWith("https://").not()
    }
}