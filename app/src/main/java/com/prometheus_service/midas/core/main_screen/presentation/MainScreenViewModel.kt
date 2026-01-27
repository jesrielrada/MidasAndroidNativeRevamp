package com.prometheus_service.midas.core.main_screen.presentation

import androidx.lifecycle.ViewModel
import com.prometheus_service.midas.core.main_screen.event.MainScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.HideSplashScreen -> {
                _uiState.update {
                    it.copy(
                        shouldDisplaySplash = false
                    )
                }
            }

            MainScreenEvent.DisplayWebviewScreen -> {
                _uiState.update {
                    it.copy(
                        shouldDisplayWebview = true
                    )
                }
            }

            MainScreenEvent.UpdatePWAReady -> {
                _uiState.update {
                    it.copy(
                        isPWAReady = true
                    )
                }
            }
        }
    }

}