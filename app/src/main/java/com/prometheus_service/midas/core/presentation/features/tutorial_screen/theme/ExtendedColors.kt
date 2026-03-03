package com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val tutorialContainer: Color,
    val tutorialIndicatorSelected: Color,
    val tutorialIndicatorUnSelected: Color,
    val tutorialOnButtonContainer: Color,
    val tutorialOnButtonContainerFinish: Color,
    val tutorialNextButton: Color,
    val tutorialFinishButton: Color,
)