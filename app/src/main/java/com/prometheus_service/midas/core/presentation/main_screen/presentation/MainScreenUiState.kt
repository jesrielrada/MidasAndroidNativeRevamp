package com.prometheus_service.midas.core.presentation.main_screen.presentation

data class MainScreenUiState(
    val shouldDisplaySplash: Boolean = true,
    val shouldDisplayTutorial: Boolean = true,
    val shouldDisplayLanguageSelection: Boolean = true,
    val shouldDisplayWebview: Boolean = false,
    val isWebviewReady: Boolean = false
)