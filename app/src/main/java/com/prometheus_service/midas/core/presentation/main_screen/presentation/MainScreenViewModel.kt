package com.prometheus_service.midas.core.presentation.main_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.core.domain.features.remote_config.use_case.GetRemoteConfig
import com.prometheus_service.midas.core.domain.features.multi_language.use_case.GetMultiLanguageData
import com.prometheus_service.midas.core.domain.features.multi_language.use_case.RefreshMultiLanguageData
import com.prometheus_service.midas.core.domain.features.remote_domains.use_case.GetRemoteDomains
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.SyncSplashTutorialImages
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val syncSplashTutorialImages: SyncSplashTutorialImages,
    private val getRemoteConfig: GetRemoteConfig,
    private val getRemoteDomains: GetRemoteDomains,
    private val refreshMultiLanguageData: RefreshMultiLanguageData,
    private val getMultiLanguageData: GetMultiLanguageData
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val operatorId = "vn88"
            val userAgent = "VN88MobileA/1.0"
            var acceptLanguage = "en"
            val currency = "USDT"

            Timber.d("Fetching remote config...")

            getRemoteConfig.invoke(
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

            MainScreenEvent.UpdateWebviewReady -> {
                _uiState.update {
                    it.copy(
                        isWebviewReady = true
                    )
                }
            }

            MainScreenEvent.SyncSplashTutorialImages -> {
                viewModelScope.launch {
                    val operatorId = "vn88"
                    val userAgent = "VN88MobileA/1.0"
                    var acceptLanguage = "en"
                    val currency = "USDT"

                    syncSplashTutorialImages.invoke(
                        operatorId = operatorId,
                        userAgent = userAgent,
                        acceptLanguage = acceptLanguage,
                        currency = currency
                    )
                }
            }
        }
    }

}