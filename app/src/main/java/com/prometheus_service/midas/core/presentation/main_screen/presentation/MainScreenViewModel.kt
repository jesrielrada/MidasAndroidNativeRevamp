package com.prometheus_service.midas.core.presentation.main_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.SyncSplashTutorialImages
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchAppBaseUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.SetHostInterceptorUrl
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
    private val cacheAppConfig: CacheAppConfigModel,
    private val syncSplashTutorialImages: SyncSplashTutorialImages,
    private val setHostInterceptorUrl: SetHostInterceptorUrl,
    private val fetchAppBaseUrl: FetchAppBaseUrl
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(MainScreenEvent.InitializeApplication)
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.InitializeApplication -> {
                viewModelScope.launch {
                    //Set first the initial base url
                    setHostInterceptorUrl.invoke(url = FlavorConfig.DOMAINS_UAT[0])

                    val baseUrl = fetchAppBaseUrl.invoke().getOrNull()
                    if (baseUrl != null) {
                        Timber.d("Caching base url.. $baseUrl")
                        setHostInterceptorUrl.invoke(baseUrl)
                        cacheAppConfig.invoke(AppConfigModel(baseUrl = baseUrl))
                    } else {
                        Timber.d("Fetching base url failed, display retry")
                        //TODO() Retry fetching
                    }
                }
            }

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
                if (!uiState.value.isWebviewReady) {
                    _uiState.update {
                        it.copy(
                            isWebviewReady = true
                        )
                    }
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