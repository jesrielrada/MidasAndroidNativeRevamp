package com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation

import com.prometheus_service.midas.R

data class TutorialScreeUiState(
    val canDisplayScreen: Boolean = true,
    val images: List<Any> = listOf(
        R.drawable.splash_bg
    ),
    val buttonDefaultLabel: String = "Next",
    val buttonEndLabel: String = "I understand"
)
