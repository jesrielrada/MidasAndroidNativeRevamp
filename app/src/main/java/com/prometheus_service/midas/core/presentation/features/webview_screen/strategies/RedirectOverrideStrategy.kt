package com.prometheus_service.midas.core.presentation.features.webview_screen.strategies

interface RedirectOverrideStrategy {
    fun matches(url: String): Boolean
}

val redirectOverrideStrategies = listOf(
    NonAuthCallbackStrategy(),
    NonHttpsStrategy()
)