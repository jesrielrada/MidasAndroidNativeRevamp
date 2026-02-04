package com.prometheus_service.midas.core.presentation.features.game_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.event.GameScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: GameScreenEvent) {
        when (event) {
            is GameScreenEvent.OnOrientationChanged -> {
                viewModelScope.launch(dispatcherProvider.main) {
                    uiState.value.sideFabOffsetY.snapTo(0f)
                }
            }

            is GameScreenEvent.OnGloballyPositioned -> {
                val newHeight = event.coordinates.size.height
                if (uiState.value.parentHeight != newHeight) {
                    Timber.d("OnGloballyPositioned called")
                    _uiState.update {
                        it.copy(
                            parentHeight = event.coordinates.size.height
                        )
                    }
                }
            }

            is GameScreenEvent.OnSideFabGloballyPositioned -> {
                val newHeight = event.coordinates.size.height
                if (uiState.value.sideFabHeight != newHeight) {
                    Timber.d("OnSideFabGloballyPositioned called..")
                    _uiState.update {
                        it.copy(
                            sideFabHeight = event.coordinates.size.height
                        )
                    }
                }
            }

            GameScreenEvent.SetSideFabClose -> {
                _uiState.update {
                    it.copy(
                        sideFabState = GameSideFabState.CLOSED,
                        displayBackdrop = false
                    )
                }
            }

            GameScreenEvent.SetSideFabOpen -> {
                _uiState.update {
                    it.copy(
                        sideFabState = GameSideFabState.OPEN,
                        displayBackdrop = true
                    )
                }
            }

            GameScreenEvent.OnClickReturnButton -> {
                _uiState.update {
                    it.copy(
                        shouldDisplayReturnDialog = true,
                        displayBackdrop = true
                    )
                }
            }

            GameScreenEvent.HideReturnDialog -> {
                _uiState.update {
                    it.copy(
                        shouldDisplayReturnDialog = false,
                        sideFabState = GameSideFabState.CLOSED,
                        displayBackdrop = false,
                    )
                }
            }
        }
    }
}