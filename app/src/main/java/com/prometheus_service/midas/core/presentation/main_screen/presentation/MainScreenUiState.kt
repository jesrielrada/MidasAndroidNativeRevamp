package com.prometheus_service.midas.core.presentation.main_screen.presentation

data class MainScreenUiState(
    val shouldDisplaySplash: Boolean = false,
    val shouldDisplayTutorial: Boolean = false,
    val shouldDisplayLanguageSelection: Boolean = true,
    val shouldDisplayWebview: Boolean = false,
    val isPWAReady: Boolean = true //TODO
)