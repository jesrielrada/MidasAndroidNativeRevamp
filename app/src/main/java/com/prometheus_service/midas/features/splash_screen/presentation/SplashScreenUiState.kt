package com.prometheus_service.midas.features.splash_screen.presentation

import com.prometheus_service.midas.R

data class SplashScreenUiState(
    val isLoading: Boolean = true,
    val isSkipVisible: Boolean = true,
    val isProgressVisible: Boolean = false,
    val isTimerRunning: Boolean = false,
    val isScrollFinished: Boolean = false,
    val currentPercentage: Int = 0,
    val currentPauseIndex: Int = 0,
    val appVersion: String = "1.0.0",
    val skipLabel: String = "Skip",
    val images: List<Any> = listOf(
        R.drawable.splash_bg,
        R.drawable.splash_bg2,
        R.drawable.splash_bg3
    ),
    val isFinishScrolling: Boolean = false
)