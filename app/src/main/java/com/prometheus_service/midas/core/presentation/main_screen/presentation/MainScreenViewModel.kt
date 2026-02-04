package com.prometheus_service.midas.core.presentation.main_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchAppBaseUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.SetHostInterceptorUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.SyncRemoteData
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val cacheAppConfig: CacheAppConfigModel,
    private val syncRemoteData: SyncRemoteData,
    private val setHostInterceptorUrl: SetHostInterceptorUrl,
    private val fetchAppBaseUrl: FetchAppBaseUrl,
    private val getMultiLanguageData: GetMultiLanguageData,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(MainScreenEvent.InitializeApplication)
        onEvent(MainScreenEvent.InitializeTranslations)
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.InitializeTranslations -> {
                viewModelScope.launch {
                    Timber.d("Initialize translations on main screen...")
                    val locale = FlavorConfig.DEFAULT_LOCALE
                    val data = getMultiLanguageData.invoke(locale).first()
                    val errorMessage = "(Error Code: E001) ${data.errorMessages.fetchDomain}"
                    _uiState.update {
                        it.copy(
                            viewTranslations = ViewTranslations(
                                mainScreenTranslations = MainScreenTranslations(
                                    initializeErrorMessage = errorMessage,
                                    retryButtonLabel = data.generalMessages.retry
                                ),
                                tutorialScreenTranslations = TutorialScreenTranslations(
                                    buttonDefaultLabel = data.tutorialTranslations.tutorialNextButton,
                                    buttonEndLabel = data.tutorialTranslations.tutorialEndButton
                                ),
                                splashScreenTranslations = SplashScreenTranslations(
                                    skipLabel = data.splashTranslations.splashSkipButton
                                ),
                                gameScreenTranslations = GameScreenTranslations(
                                    returnDialogMessage = data.popupMessages.popupExitMessage,
                                    returnDialogConfirm = data.popupMessages.popupYes,
                                    returnDialogCancel = data.popupMessages.popupNo
                                )
                            )
                        )
                    }
                }
            }

            MainScreenEvent.InitializeApplication -> {
                viewModelScope.launch {
                    //Set first the initial base url
                    setHostInterceptorUrl.invoke(url = FlavorConfig.DOMAINS_UAT[0])
                    //Fetch and cache base url
                    val baseUrl = fetchAppBaseUrl.invoke()
                    if (baseUrl != null) {
                        Timber.d("Caching base url.. $baseUrl")
                        setHostInterceptorUrl.invoke(baseUrl)
                        cacheAppConfig.invoke(AppConfigModel(baseUrl = baseUrl))
                        _uiState.update { it.copy(isAppInitialized = true) }
                    } else {
                        Timber.d("Fetching base url failed, display retry")
                        _uiState.update {
                            it.copy(
                                isErrorDialogVisible = true
                            )
                        }
                    }
                }
            }

            MainScreenEvent.HideGameViewScreen -> {
                _uiState.update {
                    it.copy(
                        shouldDisplayGameView = false
                    )
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

            MainScreenEvent.HideLanguageSelectionScreen -> {
                _uiState.update {
                    it.copy(
                        shouldDisplayLanguageSelection = false
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

            MainScreenEvent.OnWebviewReady -> {
                if (!uiState.value.isWebviewReady) {
                    _uiState.update {
                        it.copy(
                            isWebviewReady = true
                        )
                    }
                }
            }

            is MainScreenEvent.SyncRemoteData -> {
                viewModelScope.launch {
                    syncRemoteData.invoke(event.locale)
                }
            }

            MainScreenEvent.DismissErrorDialog -> {
                _uiState.update {
                    it.copy(
                        isErrorDialogVisible = false
                    )
                }
            }
        }
    }
}