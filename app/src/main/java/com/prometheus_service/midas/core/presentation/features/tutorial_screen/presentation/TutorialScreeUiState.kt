package com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation

import com.prometheus_service.midas.R

data class TutorialScreeUiState(
    val isLoading: Boolean = false,
    val images: List<Any> = listOf(
        R.drawable.splash_bg,
        R.drawable.splash_bg2,
        R.drawable.splash_bg3
    ),
    val buttonDefaultLabel: String = "Next",
    val buttonEndLabel: String = "I understand"
)
