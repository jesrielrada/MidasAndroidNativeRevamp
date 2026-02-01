package com.prometheus_service.midas.core.presentation.features.splash_screen.presentation

import com.prometheus_service.midas.R

data class SplashScreenUiState(
    val isSkipVisible: Boolean = false,
    val isProgressVisible: Boolean = false,
    val isTimerRunning: Boolean = false,
    val currentPercentage: Int = 0,
    val currentPauseIndex: Int = 0,
    val appVersion: String = "1.0.0",
    val skipLabel: String = "Skip",
    val brandLogo: Int = R.drawable.splash_logo,
    val images: List<Any> = listOf(R.drawable.bg,)
)