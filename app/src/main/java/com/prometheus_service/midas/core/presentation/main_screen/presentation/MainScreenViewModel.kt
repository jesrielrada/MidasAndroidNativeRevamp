package com.prometheus_service.midas.core.presentation.main_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.core.domain.features.app_config.use_case.GetApplicationConfig
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.GetSplashTutorialImages
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getSplashTutorialImages: GetSplashTutorialImages,
    private val getApplicationConfig: GetApplicationConfig
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("Fetching application config...")

            val operatorId = "vn88"
            val userAgent = "VN88MobileA/1.0"
            val acceptLanguage = "en"
            val currency = "USDT"

            getApplicationConfig.invoke(
                operatorId = operatorId,
                userAgent = userAgent,
                acceptLanguage = acceptLanguage,
            )
        }
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.HideSplashScreen -> {
                _uiState.update {
                    it.copy(
                        shouldDisplaySplash = false
                    )
                }
            }

            MainScreenEvent.HideTutorialScreen -> {
                _uiState.update {
                    it.copy(
                        shouldDisplayTutorial = false
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