package com.prometheus_service.midas.core.presentation.features.game_screen.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D

enum class GameSideFabState {
    CLOSED,
    OPEN
}

data class GameScreenUiState(
    val shouldDisplayReturnDialog: Boolean = false,
    val isLoading: Boolean = true,
    val isAccountLoggedIn: Boolean = false,
    val isSideFabVisible: Boolean = true,
    val displayBackdrop: Boolean = false,
    val parentHeight: Int = 0,
    val sideFabHeight: Int = 0,
    val sideFabState: GameSideFabState = GameSideFabState.CLOSED,
    val sideFabOffsetY: Animatable<Float, AnimationVector1D> = Animatable(50f), // top margin
)
