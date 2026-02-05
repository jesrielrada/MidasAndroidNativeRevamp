package com.prometheus_service.midas.core.presentation.features.game_screen.presentation.event

import androidx.compose.ui.layout.LayoutCoordinates

sealed class GameScreenEvent {
    data class OnGloballyPositioned(val coordinates: LayoutCoordinates) : GameScreenEvent()
    data class OnSideFabGloballyPositioned(val coordinates: LayoutCoordinates) : GameScreenEvent()
    object SetSideFabOpen : GameScreenEvent()
    object SetSideFabClose : GameScreenEvent()
    object OnOrientationChanged: GameScreenEvent()
    object OnClickReturnButton: GameScreenEvent()
    object HideReturnDialog: GameScreenEvent()
    object HideProgressView: GameScreenEvent()

    object ResetUiState: GameScreenEvent()

}


