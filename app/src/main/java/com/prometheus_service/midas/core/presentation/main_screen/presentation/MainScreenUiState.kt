package com.prometheus_service.midas.core.presentation.main_screen.presentation

data class MainScreenUiState(
    val shouldDisplaySplash: Boolean = true,
    val shouldDisplayTutorial: Boolean = false,
    val shouldDisplayLanguageSelection: Boolean = false,
    val shouldDisplayWebview: Boolean = false,
    val isWebviewReady: Boolean = false
)