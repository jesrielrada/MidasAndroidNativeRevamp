package com.prometheus_service.midas.core.presentation.main_screen.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.BuildConfig
import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.CanDisplayTutorial
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.shared.connectivity.use_case.GetNetworkType
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchAppBaseUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.FormatGameUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetAccountLoggedInState
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetDomainFromUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.InitializeNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.SetHostInterceptorUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.SyncRemoteData
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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
    private val getNetworkType: GetNetworkType,
    private val getAppConfigModel: GetAppConfigModel,
    private val canDisplayTutorial: CanDisplayTutorial,
    private val getDomainFromUrl: GetDomainFromUrl,
    private val initializeNativeCookies: InitializeNativeCookies,
    private val formatGameUrl: FormatGameUrl,
    private val getAccountLoggedInState: GetAccountLoggedInState
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState = _uiState.asStateFlow()


    init {
        onEvent(MainScreenEvent.InitializeNetworkType)
        observeRequiredInitializationStates()
    }

    private fun observeRequiredInitializationStates() {
        viewModelScope.launch {// TRIGGER 1: Translations (When Network + UserAgent are ready)
            uiState
                .map { it.isNetworkReady && it.webViewScreenUiState.isUserAgentReady }
                .distinctUntilChanged() // Crucial: Only trigger when the boolean flips
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
            // TRIGGER 2: Initializing App (When Custom User Agent is built)
            uiState
                .map { it.webViewScreenUiState.customUserAgent }
                .distinctUntilChanged()
                .collect { agent ->
                    if (agent.isNotEmpty()) {
                        Timber.d("Custom user agent ready.. $agent, initializing locale ... ")
                        onEvent(MainScreenEvent.InitializeLocale)
                    }
                }
        }

        viewModelScope.launch {
            // TRIGGER 3: Initializing Locale
            uiState
                .map { it.currentLocale }
                .distinctUntilChanged()
                .collect { locale ->
                    Timber.d("Locale initialized: $locale, proceeding to initialize app ... ")
                    if (locale.isNotEmpty()) {
                        onEvent(MainScreenEvent.InitializeApplication)
                    }
                }
        }

        viewModelScope.launch {
            // TRIGGER 4: Syncing (When App is initialized)
            uiState
                .map { it.isAppInitialized to it.currentLocale }
                .distinctUntilChanged()
                .collect { (initialized, locale) ->
                    if (initialized) {
                        Timber.d("App initialized, syncing remote data ... ")
                        onEvent(MainScreenEvent.SyncRemoteData(locale))
                        onEvent(MainScreenEvent.LoadBaseUrl)
                    }
                }
        }

    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            MainScreenEvent.LoadDepositRoute -> {
                viewModelScope.launch {
                    val isLoggedIn = getAccountLoggedInState.invoke()
                    val routeName = if (isLoggedIn) "deposit-route" else "login-route"
                    val route = "javascript: window.pwa.navigate({ name: '$routeName'})"

                    onEvent(MainScreenEvent.LoadCustomRoute(route))
                }
            }

            MainScreenEvent.SetWebviewUrlLoaded -> {
                Timber.d("Setting webview url loaded ...")
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            isWebViewUrlLoaded = true
                        )
                    )
                }
            }

            MainScreenEvent.ResetCustomRoute -> {
                Timber.d("Resetting custom route ...")
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

                        Timber.d("Launching game page.. game url is: $gameUrl")

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
                    Timber.d("Loading base url ...")
                    val config = getAppConfigModel.invoke().first()
                    val domain = config.domain
                    val version = BuildConfig.VERSION_NAME
                    val baseUrl = config.baseUrl!!

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
                    val config = getAppConfigModel.invoke().first()
                    val locale = config.locale ?: FlavorConfig.DEFAULT_LOCALE
                    val isLanguageSelectionDisplayed = config.isLanguageSelectionDisplayed

                    Timber.d("Initializing locale ... $locale")
                    Timber.d("Initializing isLanguageSelectionDisplayed ... $isLanguageSelectionDisplayed")

                    if (isLanguageSelectionDisplayed == null) {
                        onEvent(MainScreenEvent.DisplayLanguageSelectionScreen)
                    }

                    onEvent(MainScreenEvent.SetLocaleSelected(locale))
                }
            }


            MainScreenEvent.InitializeNetworkType -> {
                viewModelScope.launch {
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

            MainScreenEvent.OnWebviewReady -> {
                if (!uiState.value.webViewScreenUiState.isWebviewReady) {
                    Timber.d("Webview ready, updating state...")
                    _uiState.update {
                        it.copy(
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                isWebviewReady = true
                            )
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