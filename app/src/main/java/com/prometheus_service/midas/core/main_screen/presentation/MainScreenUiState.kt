package com.prometheus_service.midas.core.main_screen.presentation

data class MainScreenUiState(
    val shouldDisplaySplash: Boolean = true,
    val shouldDisplayTutorial: Boolean = false,
    val shouldDisplayWebview: Boolean = false,
    val isPWAReady: Boolean = true //TODO
)