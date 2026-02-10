package com.prometheus_service.midas.core.presentation.main_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.BuildConfig
import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.domain.shared.google_login.use_cases.GetGoogleAuthUrl
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.CanDisplayTutorial
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.shared.connectivity.use_case.GetNetworkType
import com.prometheus_service.midas.core.domain.shared.core.use_case.CacheAppCurrency
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchAppBaseUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.FormatGameUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetDomainFromUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.InitializeNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.PersistNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.SetHostInterceptorUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.SyncRemoteData
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


private data class InitializationValues(
    val isAppReady: Boolean,
    val customUserAgent: String,
    val currentLocale: String,
    val initializationResponse: Pair<Boolean, String>
)

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val cacheAppConfig: CacheAppConfigModel,
    private val syncRemoteData: SyncRemoteData,
    private val setHostInterceptorUrl: SetHostInterceptorUrl,
    private val fetchAppBaseUrl: FetchAppBaseUrl,
    private val getMultiLanguageData: GetMultiLanguageData,
    private val getNetworkType: GetNetworkType,
    private val getAppConfigModel: GetAppConfigModel,
    private val canDisplayTutorial: CanDisplayTutorial,
    private val getDomainFromUrl: GetDomainFromUrl,
    private val initializeNativeCookies: InitializeNativeCookies,
    private val formatGameUrl: FormatGameUrl,
    private val getGoogleAuthUrl: GetGoogleAuthUrl,
    private val cacheAppCurrency: CacheAppCurrency,
    private val persistNativeCookies: PersistNativeCookies,
    @Named("google_client_id") val googleClientId: String
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<MainScreenSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private var networkJob: Job? = null

    init {
        onEvent(MainScreenEvent.InitializeNetworkType)
        observeRequiredInitializationStates()
    }

    private fun observeRequiredInitializationStates() {
        viewModelScope.launch {
            uiState.map { it.isNetworkReady && it.webViewScreenUiState.isUserAgentReady }
                .distinctUntilChanged()
                .collect { ready ->
                    if (ready) {
                        Timber.d("Network and user agent ready, building user agent ... ")
                        onEvent(MainScreenEvent.InitializeTutorialSettings)
                        onEvent(MainScreenEvent.InitializeTranslations)
                        onEvent(MainScreenEvent.BuildUserAgent)
                    }
                }
        }

        viewModelScope.launch {
            uiState.map { it.webViewScreenUiState.customUserAgent }
                .distinctUntilChanged()
                .collect { agent ->
                    if (agent.isNotEmpty()){
                        Timber.d("Custom user agent ready.. $agent, initializing locale ... ")
                        onEvent(MainScreenEvent.InitializeLocale)
                    }
                }
        }

        viewModelScope.launch {
            uiState.map { it.currentLocale }
                .distinctUntilChanged()
                .collect { currentLocale ->
                    if (currentLocale.isNotEmpty()) {
                        Timber.d("Locale initialized: $currentLocale, proceeding to initialize app ... ")
                        onEvent(MainScreenEvent.InitializeApplication)
                    }
                }
        }

        viewModelScope.launch {
            uiState.map { it.isAppInitialized}
                .distinctUntilChanged()
                .collect { isAppInitialized ->
                    if (isAppInitialized) {
                        Timber.d("App initialized, loading base url  ... ")
                        onEvent(MainScreenEvent.LoadBaseUrl)
                    }
                }
        }
    }

    fun emitSideEffect(effect: MainScreenSideEffect) = viewModelScope.launch {
        _sideEffect.emit(effect)
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.HandleStoreCredentials -> {
                viewModelScope.launch {
                    Timber.d("Handling store credentials ... ${event.data}")
                    syncRemoteData.invoke(uiState.value.currentLocale)
                }
            }

            is MainScreenEvent.HandlePwaReady -> {
                viewModelScope.launch {
                    Timber.d("Handling pwa ready ... ${event.data}")
                    _uiState.update {
                        it.copy(
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                isPwaReady = true
                            )
                        )
                    }
                    persistNativeCookies.invoke()
                    cacheAppCurrency.invoke(event.data)
                    syncRemoteData.invoke(uiState.value.currentLocale)
                }
            }

            is MainScreenEvent.ProcessGoogleLogin -> {
                viewModelScope.launch {
                    val url = event.url
                    val clientId = googleClientId
                    val result = getGoogleAuthUrl.invoke(clientId, url, event.response)

                    result.onSuccess { authUrl ->
                        _uiState.update {
                            it.copy(
                                webViewScreenUiState = it.webViewScreenUiState.copy(
                                    customUrl = authUrl
                                )
                            )
                        }
                    }.onFailure {
                        Timber.d("Failure on processing google login")
                    }
                }
            }

            is MainScreenEvent.LoadCustomUrl -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                customRoute = event.customUrl
                            )
                        )
                    }
                }
            }

            is MainScreenEvent.ResetCustomUrl -> {
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            customUrl = null
                        )
                    )
                }
            }

            MainScreenEvent.SetWebviewUrlLoaded -> {
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            isWebViewUrlLoaded = true
                        )
                    )
                }
            }

            MainScreenEvent.ResetCustomRoute -> {
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            customRoute = null
                        )
                    )
                }
            }

            is MainScreenEvent.LoadCustomRoute -> {
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            customRoute = event.route
                        )
                    )
                }
            }

            is MainScreenEvent.LaunchGamePage -> {
                viewModelScope.launch {
                    val webViewUrl = uiState.value.webViewScreenUiState.webviewUrl
                    webViewUrl?.let { url ->
                        val gameUrl = formatGameUrl.invoke(
                            gamePath = event.gamePath,
                            baseUrl = url
                        )

                        _uiState.update {
                            it.copy(
                                shouldDisplayGameView = true,
                                gameUrl = gameUrl
                            )
                        }
                    }
                }
            }

            MainScreenEvent.LoadBaseUrl -> {
                viewModelScope.launch {
                    val config = getAppConfigModel.invoke().firstOrNull()
                    val domain = config?.domain
                    val version = BuildConfig.VERSION_NAME
                    val baseUrl = config?.baseUrl

                    initializeNativeCookies.invoke(
                        domain = domain!!,
                        version = version,
                        language = uiState.value.currentLocale
                    )

                    _uiState.update {
                        it.copy(
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                webviewUrl = baseUrl
                            )
                        )
                    }
                }
            }

            MainScreenEvent.InitializeTutorialSettings -> {
                viewModelScope.launch {
                    if (canDisplayTutorial.invoke()) {
                        _uiState.update {
                            it.copy(
                                canDisplayTutorialScreen = true
                            )
                        }
                    }
                }
            }

            MainScreenEvent.DisplayTutorialScreen -> {
                _uiState.update {
                    it.copy(
                        shouldDisplayTutorial = true
                    )
                }
            }

            is MainScreenEvent.SetLocaleSelected -> {
                _uiState.update {
                    it.copy(
                        currentLocale = event.locale
                    )
                }
            }

            MainScreenEvent.DisplayLanguageSelectionScreen -> {
                _uiState.update {
                    it.copy(
                        shouldDisplayLanguageSelection = true
                    )
                }
            }

            MainScreenEvent.InitializeLocale -> {
                viewModelScope.launch {
                    val config = getAppConfigModel.invoke().firstOrNull()
                    val locale = config?.locale ?: FlavorConfig.DEFAULT_LOCALE
                    val isLanguageSelectionDisplayed = config?.isLanguageSelectionDisplayed

                    if (isLanguageSelectionDisplayed == null) {
                        onEvent(MainScreenEvent.DisplayLanguageSelectionScreen)
                    }

                    onEvent(MainScreenEvent.SetLocaleSelected(locale))
                }
            }


            MainScreenEvent.InitializeNetworkType -> {
                networkJob?.cancel()
                networkJob = viewModelScope.launch {
                    getNetworkType.invoke().collect { networkType ->
                        Timber.d("Initializing network ... $networkType")
                        if (networkType.isNotEmpty()) {
                            _uiState.update {
                                it.copy(
                                    networkType = networkType,
                                    isNetworkReady = true
                                )
                            }
                        }
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
                        val domain = getDomainFromUrl.invoke(baseUrl)
                        setHostInterceptorUrl.invoke(baseUrl)
                        cacheAppConfig.invoke(AppConfigModel(baseUrl = baseUrl, domain = domain))
                        _uiState.update {
                            it.copy(
                                isAppInitialized = true
                            )
                        }
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

            is MainScreenEvent.SetUserAgentReady -> {
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            webviewUserAgent = event.userAgent,
                            isUserAgentReady = true
                        )
                    )
                }
            }

            is MainScreenEvent.BuildUserAgent -> {
                Timber.d("Building user agent ... ")
                val customUserAgent = ("${uiState.value.webViewScreenUiState.webviewUserAgent} " +
                        "${FlavorConfig.INITIAL_USER_AGENT} " +
                        uiState.value.networkType).trimEnd()
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            customUserAgent = customUserAgent
                        ),
                    )
                }
            }

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