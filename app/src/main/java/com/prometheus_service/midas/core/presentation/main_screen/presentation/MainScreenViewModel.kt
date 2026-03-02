package com.prometheus_service.midas.core.presentation.main_screen.presentation

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.BuildConfig
import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.CacheSecondStageConfig
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.CanDisplayPinlock
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.GetSecondStageConfig
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.CanDisplayTutorial
import com.prometheus_service.midas.core.domain.providers.CookieProvider
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.DeleteBestDomain
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.AccountDisplayResult
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.AccountSelectedResult
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.EnrollmentResult
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.GetBiometricCurrentAccount
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleAccountDeletion
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleAccountSelectedAuthSucceed
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricAccountDisplay
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricAccountSelected
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricAuthCancelled
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricAuthError
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricButtonDisplay
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricsEnrollment
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.InitializeBiometricsPrompt
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.PersistBiometricsUser
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.SetBiometricsEnabled
import com.prometheus_service.midas.core.domain.shared.connectivity.use_case.GetNetworkType
import com.prometheus_service.midas.core.domain.shared.connectivity.use_case.ObserveNetwork
import com.prometheus_service.midas.core.domain.shared.core.use_case.CacheAppCurrency
import com.prometheus_service.midas.core.domain.shared.core.use_case.CanDisplayMinimumOsDialog
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchAppBaseUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.FormatGameUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetAccountLoggedInState
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetConfigDomains
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetDomainFromUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.InitializeNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.PersistNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.SetHostInterceptorUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.SyncRemoteData
import com.prometheus_service.midas.core.domain.shared.google_login.use_cases.GetGoogleAuthUrl
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.LoadCustomRoute
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenSideEffect
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.BiometricsTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.DefaultErrorTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.DownloadTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.GameScreenTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.MainScreenTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.MinimumOSTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.SecondStageTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.SplashScreenTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.TutorialScreenTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.ViewTranslations
import com.prometheus_service.midas.core.presentation.main_screen.presentation.util.Constants
import com.prometheus_service.midas.core.presentation.main_screen.presentation.util.Constants.Companion.DISPLAY_BIOMETRICS_SCRIPT
import com.prometheus_service.midas.core.presentation.main_screen.presentation.util.Constants.Companion.HIDE_BIOMETRICS_SCRIPT
import com.prometheus_service.midas.core.presentation.main_screen.presentation.util.Constants.Companion.LOGIN_LAUNCHER_SCRIPT
import com.prometheus_service.midas.core.presentation.main_screen.presentation.util.Constants.Companion.LOGIN_ROUTE
import com.prometheus_service.midas.core.presentation.main_screen.presentation.util.Constants.Companion.togglePinCodeStorageScript
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


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
    private val handleBiometricsEnrollment: HandleBiometricsEnrollment,
    private val initializeBiometricsPrompt: InitializeBiometricsPrompt,
    private val setBiometricsEnabled: SetBiometricsEnabled,
    private val persistBiometricsUser: PersistBiometricsUser,
    private val handleBiometricButtonDisplay: HandleBiometricButtonDisplay,
    private val handleBiometricAccountDisplay: HandleBiometricAccountDisplay,
    private val handleBiometricAccountSelected: HandleBiometricAccountSelected,
    private val handleBiometricAccountSelectedAuthSucceed: HandleAccountSelectedAuthSucceed,
    private val getBiometricCurrentAccount: GetBiometricCurrentAccount,
    private val handleBiometricAuthError: HandleBiometricAuthError,
    private val handleBiometricAuthCancelled: HandleBiometricAuthCancelled,
    private val cacheSecondStageConfig: CacheSecondStageConfig,
    private val canDisplayPinlock: CanDisplayPinlock,
    private val getSecondStageConfig: GetSecondStageConfig,
    private val cookieProvider: CookieProvider,
    private val observeNetwork: ObserveNetwork,
    private val getConfigDomains: GetConfigDomains,
    private val deleteBestDomain: DeleteBestDomain,
    private val getAccountLoggedInState: GetAccountLoggedInState,
    private val canDisplayMinimumOsDialog: CanDisplayMinimumOsDialog,
    private val handleAccountDeletion: HandleAccountDeletion,
    @param:Named("google_client_id") val googleClientId: String
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
                    if (agent.isNotEmpty()) {
                        Timber.d("Custom user agent ready.. $agent, initializing locale ... ")
                        onEvent(MainScreenEvent.InitializeLocale)
                    }
                }
        }

        viewModelScope.launch {
            uiState.map { it.currentLocale }
                .distinctUntilChanged()
                .collect { currentLocale ->
                    if (currentLocale.isNotEmpty() && uiState.value.isAppInitialized.not()) {
                        Timber.d("Locale initialized: $currentLocale, proceeding to initialize app ... ")
                        onEvent(MainScreenEvent.InitializeApplication)
                    }
                }
        }

        viewModelScope.launch {
            uiState.map { it.isAppInitialized }
                .distinctUntilChanged()
                .collect { isAppInitialized ->
                    if (isAppInitialized) {
                        val config = getAppConfigModel.invoke().firstOrNull()
                        val isLanguageSelectionDisplayed = config?.isLanguageSelectionDisplayed

                        if (isLanguageSelectionDisplayed == null) {
                            Timber.d("App initialized, displaying language selection  ... ")
                            onEvent(MainScreenEvent.DisplayLanguageSelectionScreen)
                        } else {
                            Timber.d("App initialized, loading base url  ... ")
                            onEvent(MainScreenEvent.LoadBaseUrl)
                        }
                    }
                }
        }
    }

    fun emitSideEffect(effect: MainScreenSideEffect) = viewModelScope.launch {
        _sideEffect.emit(effect)
    }

    fun updateMainState(state: (MainScreenUiState) -> MainScreenUiState) {
        _uiState.update(state)
    }

    fun onEvent(event: MainScreenEvent) {
        when (event) {
            is MainScreenEvent.HandleShouldDisplaySecondStage -> {
                viewModelScope.launch {
                    cacheSecondStageConfig.invoke(
                        SecondStageModel(
                            isCmsboEnabled = event.enabled
                        )
                    )
                }
            }

            is MainScreenEvent.HandleResetCredentials -> {
                viewModelScope.launch {
                    handleAccountDeletion.invoke(event.data)
                }
            }

            MainScreenEvent.HandleMinimumOsDialogDismiss -> {
                viewModelScope.launch {
                    cacheAppConfig.invoke(
                        AppConfigModel(
                            isMinimumOsDialogHideToggled = true
                        )
                    )
                    _uiState.update {
                        it.copy(
                            canDisplayMinimumOsDialog = false
                        )
                    }
                }
            }

            is MainScreenEvent.MemberLoggedIn -> {
                Timber.d("Member logged in ... ${event.data}")
                viewModelScope.launch {
                    val config = getAppConfigModel.invoke().first()
                    val locale = config.locale ?: FlavorConfig.DEFAULT_LOCALE
                    val version = BuildConfig.VERSION_NAME
                    val domain = config.domain
                    val remoteData = event.data
                    val key = FlavorConfig.OPERATOR_ID
                    val currentRoute = uiState.value.currentRoute

                    initializeNativeCookies.invoke(
                        domain = domain!!,
                        version = version,
                        language = locale
                    )

                    uiState.value.webViewScreenUiState.webviewUrl
                        ?.let {
                            val cookies = cookieProvider.getCurrentCookies(it)
                            Timber.d("Current cookies are: $cookies")
                            cookies
                        }
                        ?.let { it ->
                            Timber.d("Caching session cookies ... $it")
                            cacheAppConfig(AppConfigModel(sessionCookies = it))
                        }

                    uiState.value.launcherUrl?.let {
                        Timber.d("Handling launcher url after logged in ... $it")
                        onEvent(MainScreenEvent.HandlePushNotificationUrl(it))
                    }


                    handleBiometricsEnrollment.invoke(
                        data = remoteData,
                        key = key,
                        currentRoute = currentRoute
                    ).onSuccess { result ->
                        when (result) {
                            EnrollmentResult.NonEnrolled -> {
                                _uiState.update {
                                    it.copy(
                                        isBiometricsEnableDialogVisible = true
                                    )
                                }
                            }

                            is EnrollmentResult.UpdateEnrollment -> {
                                _sideEffect.emit(
                                    MainScreenSideEffect.DisplayBiometricPrompt(
                                        result.cipher
                                    )
                                )
                            }

                            is EnrollmentResult.FailedUpdateEnrollment -> {
                                displayBiometricFailedDialog()
                            }
                        }
                    }.onFailure { e ->
                        Timber.e("Failure handling store credentials, ${e.localizedMessage}")
                    }

                    val cachedCredentials = getSecondStageConfig.invoke().firstOrNull()?.credentials
                    val isCredentialsUpdated =
                        cachedCredentials.isNullOrEmpty().not() && cachedCredentials != event.data

                    if (isCredentialsUpdated) {
                        val script = togglePinCodeStorageScript(false)
                        Timber.d("Pincode disabled, setting to false ... $script")
                        _uiState.update {
                            it.copy(
                                webViewScreenUiState = it.webViewScreenUiState.copy(
                                    customScript = script,
                                    customCallbackScript = null
                                )
                            )
                        }
                        cacheSecondStageConfig.invoke(
                            SecondStageModel(
                                isUserEnabled = false,
                                pin = ""
                            )
                        )
                    }

                    cacheSecondStageConfig.invoke(SecondStageModel(credentials = event.data))

                    val canDisplayPinlock = canDisplayPinlock.invoke(
                        baseUrl = uiState.value.webViewScreenUiState.webviewUrl,
                        pwaReady = uiState.value.webViewScreenUiState.isPwaReady
                    )

                    if (canDisplayPinlock) {
                        _uiState.update { it.copy(shouldDisplaySecondStage = true) }
                    }

                    syncRemoteData.invoke(uiState.value.currentLocale)
                    _uiState.update { it.copy(onDataSync = true) }
                }
            }

            is MainScreenEvent.HandlePushNotificationUrl -> {
                viewModelScope.launch {
                    val pushUrl = event.url
                    val baseUrl = getAppConfigModel.invoke().first().baseUrl
                    val isLoggedIn = getAccountLoggedInState.invoke()

                    val convertedUrl = when {
                        pushUrl.contains("tracker") -> {
                            val id = pushUrl.substringAfterLast('/')
                            "$baseUrl/tracker/$id"
                        }

                        pushUrl.contains("affiliateId") -> {
                            val affiliateId = pushUrl
                                .substringAfterLast('=')
                                .substringAfterLast('/')
                            "$baseUrl/?affiliateid=$affiliateId"
                        }

                        else -> pushUrl
                    }

                    Timber.d("Handling push notification url ... $convertedUrl")

                    if (convertedUrl.contains("launcher")) {
                        if (isLoggedIn) {
                            Timber.d("Handling launcher url ... loggedIn $convertedUrl")
                            _uiState.update {
                                it.copy(
                                    launcherUrl = "",
                                    webViewScreenUiState = it.webViewScreenUiState.copy(
                                        customUrl = convertedUrl
                                    )
                                )
                            }
                        } else {
                            Timber.d("Handling launcher url ... loggedOut")
                            _uiState.update {
                                it.copy(
                                    launcherUrl = convertedUrl,
                                    webViewScreenUiState = it.webViewScreenUiState.copy(
                                        customScript = LOGIN_ROUTE
                                    )
                                )
                            }
                        }
                    } else if (convertedUrl.contains("referralId") ||
                        convertedUrl.contains("register.aspx")
                    ) {
                        Timber.d("Handling referral id ...")
                        onEvent(MainScreenEvent.HandleOpenInBrowser(convertedUrl))
                    } else if (convertedUrl.contains("affiliateid")) {
                        Timber.d("Handling affiliate id ...")
                        _uiState.update {
                            it.copy(
                                webViewScreenUiState = it.webViewScreenUiState.copy(
                                    customUrl = convertedUrl
                                )
                            )
                        }
                    } else {
                        Timber.d("Handling push notification url else condition...")
                        _uiState.update {
                            it.copy(
                                webViewScreenUiState = it.webViewScreenUiState.copy(
                                    customScript = convertedUrl
                                )
                            )
                        }
                    }
                }
            }

            MainScreenEvent.CacheSessionCookies -> {
                viewModelScope.launch {
                    uiState.value.webViewScreenUiState.webviewUrl
                        ?.let {
                            cookieProvider.persistCookies()
                            delay(500)

                            val cookies = cookieProvider.getCurrentCookies(it)
                            Timber.d("Current cookies are: $cookies, next caching cookies")
                            cookies
                        }
                        ?.let { it ->
                            Timber.d("Caching session cookies ... $it")
                            cacheAppConfig(AppConfigModel(sessionCookies = it))
                        }
                }
            }

            MainScreenEvent.HandleGeoBlockMode -> {
                viewModelScope.launch {
                    val config = getAppConfigModel.invoke()
                    val locale = config.first().locale ?: FlavorConfig.DEFAULT_LOCALE
                    val translations = getMultiLanguageData.invoke(locale).first()
                    updateMainState {
                        it.copy(
                            isDefaultErrorDialogVisible = true,
                            viewTranslations = it.viewTranslations.copy(
                                defaultErrorTranslations = it.viewTranslations.defaultErrorTranslations.copy(
                                    dialogMessage = translations.generalMessages.geoblockedDialog,
                                    dialogBtn = translations.generalMessages.ok
                                )
                            )
                        )
                    }
                }
            }

            MainScreenEvent.HandleMaintenanceMode -> {
                viewModelScope.launch {
                    val config = getAppConfigModel.invoke()
                    val locale = config.first().locale ?: FlavorConfig.DEFAULT_LOCALE
                    val translations = getMultiLanguageData.invoke(locale).first()
                    updateMainState {
                        it.copy(
                            isDefaultErrorDialogVisible = true,
                            viewTranslations = it.viewTranslations.copy(
                                defaultErrorTranslations = it.viewTranslations.defaultErrorTranslations.copy(
                                    dialogMessage = translations.generalMessages.maintenanceDialog,
                                    dialogBtn = translations.generalMessages.ok
                                )
                            )
                        )
                    }
                }
            }

            is MainScreenEvent.HandleLaunchNewWindow -> {
                val helperUrl = initHelperScreenUrl(url = event.url)
                Timber.d("Handling launch new window, callback url ... $helperUrl")
                _uiState.update {
                    it.copy(
                        shouldDisplayHelperScreen = true,
                        helperUiState = it.helperUiState.copy(
                            url = helperUrl
                        )
                    )
                }
            }

            is MainScreenEvent.HandleOpenInBrowser -> {
                val callbackUrl = initCallbackUrl(url = event.url)
                if (isHttpOrSocial(event.url.lowercase())) {
                    emitSideEffect(MainScreenSideEffect.StartActionView(event.url))
                } else {
                    emitSideEffect(MainScreenSideEffect.StartActionView(callbackUrl))
                }
            }

            is MainScreenEvent.HandleSwitchLanguage -> {
                viewModelScope.launch {
                    val locale = event.language.removeSurrounding("\"")
                    Timber.d("Handling switch language ..., $locale")
                    cacheAppConfig.invoke(AppConfigModel(locale = locale))
                    syncRemoteData.invoke(locale)
                    _uiState.update { it.copy(onDataSync = true) }
                }
            }

            MainScreenEvent.HandleSecondStageMaxAttempt -> {
                viewModelScope.launch {

                    Timber.d("Handling max attempt ... ")
                    //val script = togglePinCodeStorageScript(false)

                    _uiState.update {
                        it.copy(
                            shouldDisplaySecondStage = false,
                            shouldDisplayTutorial = false,
                            canDisplayTutorialScreen = false
                        )
                    }
                    cacheSecondStageConfig.invoke(
                        SecondStageModel(
                            isUserEnabled = false,
                            pin = "",
                            credentials = ""
                        )
                    )

                    val domain = getAppConfigModel.invoke().first().domain
                    val logoutScript = """
                        javascript:(function() {
                        var expiry = "; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=$domain";
                        document.cookie = "s=" + expiry;
                        document.cookie = "pt_token=" + expiry;
                         })();
                        """.trimIndent()

                    _uiState.update {
                        it.copy(
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                customScript = logoutScript
                            )
                        )
                    }

                    Timber.d("Loading logout script  ...")

                    delay(2000L)

                    _uiState.update {
                        it.copy(
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                shouldReloadWebview = true
                            )
                        )
                    }

                    Timber.d("Reloading webview ... ")

                }
            }

            MainScreenEvent.HandleMemberLoggedOut -> {
                Timber.d("Handling member logged out... ")
                viewModelScope.launch {
                    onEvent(MainScreenEvent.CacheSessionCookies)

                    val script = togglePinCodeStorageScript(false)
                    _uiState.update {
                        it.copy(
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                customScript = script
                            )
                        )
                    }
                    cacheSecondStageConfig.invoke(
                        SecondStageModel(
                            isUserEnabled = false,
                            pin = "",
                            credentials = ""
                        )
                    )
                }
            }

            is MainScreenEvent.HandleCustomScriptCallback -> {
                Timber.d("Handling custom script callback ... ${event.data}")
                viewModelScope.launch {
                    // Used on for force logout
                    _uiState.update {
                        it.copy(
                            shouldDisplaySplash = false,
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                customCallbackScript = null
                            )
                        )
                    }
                }
            }

            MainScreenEvent.HandleOnResume -> {
                viewModelScope.launch {
                    val canDisplayPinlock = canDisplayPinlock.invoke(
                        baseUrl = uiState.value.webViewScreenUiState.webviewUrl,
                        pwaReady = uiState.value.webViewScreenUiState.isPwaReady
                    )

                    Timber.d("Handling on resume ... $canDisplayPinlock")

                    if (canDisplayPinlock) {
                        _uiState.update {
                            it.copy(
                                shouldDisplaySecondStage = true
                            )
                        }
                    }
                }
            }

            MainScreenEvent.HandlePinCodeToggleOff -> {
                val script = togglePinCodeStorageScript(false)
                _uiState.update {
                    it.copy(
                        shouldDisplaySecondStage = false,
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            customScript = script
                        )
                    )
                }
            }

            is MainScreenEvent.HandlePinCodeToggled -> {
                viewModelScope.launch {
                    val script = togglePinCodeStorageScript(event.enabled)
                    _uiState.update {
                        it.copy(
                            shouldDisplaySecondStage = event.enabled,
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                customScript = script
                            )
                        )
                    }

                    Timber.d("Handling pin code toggled ... ${event.enabled}")
                    if (event.enabled) {
                        cacheSecondStageConfig.invoke(
                            SecondStageModel(
                                isUserEnabled = true
                            )
                        )
                    } else {
                        Timber.d("Handling pin code toggled ... disabling")
                        cacheSecondStageConfig.invoke(
                            SecondStageModel(
                                isUserEnabled = false,
                                pin = ""
                            )
                        )
                    }
                }
            }

            MainScreenEvent.HandleAccountSelectionAuthCancelled -> {
                viewModelScope.launch {
                    val locale = uiState.value.currentLocale
                    handleBiometricAuthCancelled.invoke(locale)
                        .onSuccess {
                            displayBiometricNoneEnrolled()
                        }.onFailure {
                            Timber.e("Failure handling account selection auth dismissed")
                        }
                }
            }

            is MainScreenEvent.HandleBiometricsAuthError -> {
                viewModelScope.launch {
                    val locale = uiState.value.currentLocale
                    val currentRoute = uiState.value.currentRoute
                    _sideEffect.emit(
                        MainScreenSideEffect.DisplayBiometricAuthError(
                            event.code,
                            event.message
                        )
                    )
                    handleBiometricAuthError.invoke(
                        currentRoute = currentRoute,
                        locale = locale
                    )
                }
            }

            MainScreenEvent.HideBiometricErrorDialog -> {
                _uiState.update {
                    it.copy(
                        isBiometricsErrorDialogVisible = false
                    )
                }
            }

            MainScreenEvent.HideBiometricEnableDialog -> {
                _uiState.update {
                    it.copy(
                        isBiometricsEnableDialogVisible = false
                    )
                }
            }

            MainScreenEvent.SetBiometricsDisabled -> {
                viewModelScope.launch {
                    setBiometricsEnabled.invoke(uiState.value.currentLocale, false)
                }
            }

            MainScreenEvent.HandleBiometricsLogin -> {
                viewModelScope.launch {
                    val currentAccount = getBiometricCurrentAccount.invoke()
                    _uiState.update {
                        it.copy(
                            biometricCurrentAccount = currentAccount,
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                customRoute = LOGIN_ROUTE
                            )
                        )
                    }

                    if (currentAccount != null) {
                        val script = Constants.authenticateScript(
                            memberCode = currentAccount.memberCode!!,
                            password = currentAccount.bio!!
                        )

                        Timber.d("Handling biometric login ..., authenticating.. $script")

                        _uiState.update {
                            it.copy(
                                isBiometricsLoadingDialogVisible = true,
                                webViewScreenUiState = it.webViewScreenUiState.copy(
                                    customScript = script
                                )
                            )
                        }

                        delay(500)

                        _uiState.update {
                            it.copy(
                                isBiometricsLoadingDialogVisible = false
                            )
                        }
                    }
                }
            }

            is MainScreenEvent.HandleAccountSelectedAuthSucceed -> {
                viewModelScope.launch {
                    handleBiometricAccountSelectedAuthSucceed.invoke(event.result)
                        .onSuccess { result ->
                            _uiState.update {
                                it.copy(
                                    webViewScreenUiState = it.webViewScreenUiState.copy(
                                        customScript = LOGIN_LAUNCHER_SCRIPT
                                    )
                                )
                            }
                        }.onFailure {
                            Timber.e(
                                "Failure handling account " +
                                        "selected auth succeed ${it.localizedMessage}"
                            )
                        }
                }
            }

            is MainScreenEvent.HandleAccountSelected -> {
                viewModelScope.launch {
                    Timber.d("Handling account selected ...")
                    val key = FlavorConfig.OPERATOR_ID
                    val locale = uiState.value.currentLocale
                    handleBiometricAccountSelected.invoke(
                        key = key,
                        username = event.username,
                        locale = locale
                    ).onSuccess { result ->
                        when (result) {
                            is AccountSelectedResult.Authorized -> {
                                Timber.d("Account selected authorized")
                                _sideEffect.emit(
                                    MainScreenSideEffect.DisplayBiometricPrompt(
                                        result.cipher,
                                        true
                                    )
                                )
                            }

                            is AccountSelectedResult.NonEnrolled -> {
                                Timber.d("Account selected, displaying non enrolled")
                                displayBiometricNoneEnrolled()
                            }
                        }
                    }.onFailure {
                        Timber.e("Failure handling account selected ${it.localizedMessage}")
                    }
                }
            }

            MainScreenEvent.DisplayBiometricAccountSelection -> {
                viewModelScope.launch {
                    Timber.d("Displaying biometric account selection ...")
                    val locale = uiState.value.currentLocale
                    handleBiometricAccountDisplay.invoke(locale)
                        .onSuccess { result ->
                            when (result) {
                                is AccountDisplayResult.AccountDisplayList -> {
                                    _sideEffect.emit(
                                        MainScreenSideEffect.DisplayBiometricSelectionList(
                                            result.usernames
                                        )
                                    )
                                }

                                AccountDisplayResult.DisplayNoneEnrolled -> {
                                    Timber.d("Displaying none enrolled")
                                    displayBiometricNoneEnrolled()
                                }
                            }
                        }
                        .onFailure {
                            Timber.e(
                                "Failure displaying biometric " +
                                        "account selection ${it.localizedMessage}"
                            )
                        }
                }
            }

            is MainScreenEvent.HandleBiometricsAuthResult -> {
                viewModelScope.launch {
                    Timber.d("Handling biometric auth result ...")
                    persistBiometricsUser.invoke(event.result)
                        .onSuccess {
                            Timber.d("Success persisting biometrics user")
                            _sideEffect.emit(MainScreenSideEffect.DisplayBiometricSuccessEnrollment)
                        }.onFailure { e ->
                            Timber.e(e, "Failed persisting biometrics user")
                        }
                }
            }

            is MainScreenEvent.InitializeBiometricPrompt -> {
                viewModelScope.launch {
                    Timber.d("Initializing biometric prompt ...")

                    val locale = uiState.value.currentLocale
                    val key = FlavorConfig.OPERATOR_ID

                    setBiometricsEnabled.invoke(locale, true)
                    initializeBiometricsPrompt.invoke(key)
                        .onSuccess { cipher ->
                            cipher?.let {
                                Timber.d("Success initializing biometric prompt")
                                _sideEffect.emit(
                                    MainScreenSideEffect.DisplayBiometricPrompt(cipher)
                                )
                            }
                        }.onFailure {
                            Timber.d("Failure initializing biometric prompt")
                            displayBiometricFailedDialog()
                        }
                }
            }

            is MainScreenEvent.UpdateCurrentRoute -> {
                Timber.d("Updating current route ... ${event.route}")
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            currentRoute = event.route
                        )
                    }

                    if (event.route == "login-route") {
                        Timber.d("Handling biometric button display ...")
                        handleBiometricButtonDisplay.invoke()
                            .onSuccess {
                                _uiState.update {
                                    it.copy(
                                        webViewScreenUiState = it.webViewScreenUiState.copy(
                                            customScript = DISPLAY_BIOMETRICS_SCRIPT
                                        )
                                    )
                                }
                            }.onFailure {
                                _uiState.update {
                                    it.copy(
                                        webViewScreenUiState = it.webViewScreenUiState.copy(
                                            customScript = HIDE_BIOMETRICS_SCRIPT
                                        )
                                    )
                                }

                                Timber.e(it, "Hiding biometric button display")
                            }
                    }
                }

            }

            is MainScreenEvent.HandlePwaReady -> {
                viewModelScope.launch {
                    observeNetwork.invoke().collect { status ->
                        _uiState.update {
                            it.copy(
                                shouldDisplayNetworkError = !status.isConnected
                            )
                        }
                    }
                }

                viewModelScope.launch {
                    Timber.d("Handling pwa ready ...")
                    val locale =
                        getAppConfigModel.invoke().first().locale ?: FlavorConfig.DEFAULT_LOCALE
                    val currentVersion = Build.VERSION.RELEASE


                    if (canDisplayMinimumOsDialog.invoke(
                            locale = locale,
                            currentVersion = currentVersion
                        )
                    ) {
                        _uiState.update { it.copy(canDisplayMinimumOsDialog = true) }
                    }


                    val isPinCodeEnabled = getSecondStageConfig.invoke().first().isUserEnabled
                    if (isPinCodeEnabled != null && !isPinCodeEnabled) {
                        val script = togglePinCodeStorageScript(false)
                        _uiState.update {
                            it.copy(
                                shouldDisplaySecondStage = false,
                                webViewScreenUiState = it.webViewScreenUiState.copy(
                                    customScript = script
                                )
                            )
                        }
                    }


                    _uiState.update {
                        it.copy(
                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                isPwaReady = true,
                            )
                        )
                    }

                    val canDisplayPinlock = canDisplayPinlock.invoke(
                        baseUrl = uiState.value.webViewScreenUiState.webviewUrl,
                        pwaReady = uiState.value.webViewScreenUiState.isPwaReady
                    )

                    if (canDisplayPinlock) {
                        _uiState.update {
                            it.copy(
                                shouldDisplaySecondStage = true
                            )
                        }
                    }


                    persistNativeCookies.invoke()
                    cacheAppCurrency.invoke(event.data)
                    syncRemoteData.invoke(uiState.value.currentLocale)

                    _uiState.update {
                        it.copy(
                            onDataSync = true
                        )
                    }
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

            MainScreenEvent.SetWebviewUrlLoaded -> {
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            isWebViewUrlLoaded = true
                        )
                    )
                }
            }

            is MainScreenEvent.ResetCustomUrl -> {
                Timber.d("Resetting custom url, script, scrip with callback  ...")
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            customUrl = null,
                            customScript = null
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

            is LoadCustomRoute -> {
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            customRoute = event.route
                        )
                    )
                }
            }

            is MainScreenEvent.LoadCustomScript -> {
                _uiState.update {
                    it.copy(
                        webViewScreenUiState = it.webViewScreenUiState.copy(
                            customScript = event.script
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

            MainScreenEvent.LoadBaseUrl -> {
                viewModelScope.launch {
                    uiState
                        .map { it.webViewScreenUiState.isPwaReady }
                        .distinctUntilChanged()
                        .collectLatest { isReady ->
                            if (!isReady) {
                                while (true) {
                                    delay(45000L)
                                    val homepageError = uiState.value.viewTranslations
                                        .mainScreenTranslations
                                        .homepageErrorMessage
                                    _uiState.update {
                                        it.copy(
                                            snackBarMessage = homepageError,
                                            webViewScreenUiState = it.webViewScreenUiState.copy(
                                                shouldReloadWebview = true
                                            )
                                        )
                                    }
                                }
                            }
                        }
                }

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

            MainScreenEvent.InitializeApplication -> {
                viewModelScope.launch {
                    val domains = getConfigDomains.invoke(
                        environment = BuildConfig.BuildEnv,
                        uatDomains = FlavorConfig.DOMAINS_UAT,
                        prodDomains = FlavorConfig.DOMAINS_PROD,
                        preprodDomains = FlavorConfig.DOMAINS_PREPROD
                    )

                    val cachedVersion = getAppConfigModel.invoke().firstOrNull()?.version
                    val latestVersion = BuildConfig.VERSION_NAME

                    if (cachedVersion != null && cachedVersion != latestVersion) {
                        Timber.d("Invalidating best domain")
                        deleteBestDomain.invoke()
                    }

                    //Fetch and cache base url
                    Timber.d("Config domains .. $domains")
                    val fetchedDomains = fetchAppBaseUrl.invoke(domains = domains)
                    if (fetchedDomains != null) {
                        val bestDomain = fetchedDomains.first
                        val baseUrl = fetchedDomains.second
                        val domain = getDomainFromUrl.invoke(baseUrl)
                        Timber.d("Fetched base url ..$baseUrl, best domain is: $bestDomain")
                        setHostInterceptorUrl.invoke(baseUrl)
                        cacheAppConfig.invoke(
                            AppConfigModel(
                                version = BuildConfig.VERSION_NAME,
                                baseUrl = baseUrl,
                                domain = domain,
                                bestDomain = bestDomain
                            )
                        )
                        _uiState.update { it.copy(isAppInitialized = true) }
                    } else {
                        Timber.d("Fetching base url failed, display retry")
                        _uiState.update { it.copy(isInitializeErrorDialogVisible = true) }
                    }
                }
            }

            MainScreenEvent.InitializeLocale -> {
                viewModelScope.launch {
                    val config = getAppConfigModel.invoke().firstOrNull()
                    val locale = config?.locale ?: FlavorConfig.DEFAULT_LOCALE
                    onEvent(MainScreenEvent.SetLocaleSelected(locale))
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

            MainScreenEvent.InitializeNetworkType -> {
                networkJob?.cancel()
                networkJob = viewModelScope.launch {
                    getNetworkType.invoke().collect { networkType ->
                        Timber.d("Initializing network ... $networkType")
                        _uiState.update {
                            it.copy(
                                networkType = networkType,
                                isNetworkReady = true
                            )
                        }
                    }
                }
            }

            MainScreenEvent.InitializeTranslations -> {
                viewModelScope.launch {
                    val config = getAppConfigModel.invoke()
                    val locale = config.first().locale ?: FlavorConfig.DEFAULT_LOCALE
                    val data = getMultiLanguageData.invoke(locale).first()
                    val errorMessage = "(Error Code: E001) ${data.errorMessages.fetchDomain}"
                    _uiState.update {
                        it.copy(
                            viewTranslations = ViewTranslations(
                                defaultErrorTranslations = DefaultErrorTranslations(
                                    dialogMessage = "",
                                    dialogBtn = ""
                                ),
                                mainScreenTranslations = MainScreenTranslations(
                                    networkErrorMessage = data.errorMessages.network,
                                    homepageErrorMessage = data.errorMessages.homepage,
                                    initializeErrorMessage = errorMessage,
                                    retryButtonLabel = data.generalMessages.retry,
                                    exitButtonLabel = data.generalMessages.exitApp
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
                                ),
                                biometricsTranslations = BiometricsTranslations(
                                    dialogSelectAccount = data.biometricsTranslations.biometricsSelectAccount,
                                    promptTitle = data.biometricsTranslations.promptInfoTitle,
                                    promptCancel = data.biometricsTranslations.promptInfoCancel,
                                    dialogEnableTitle = data.biometricsTranslations.biometricsAlertTitle,
                                    dialogEnableMessage = data.biometricsTranslations.biometricsAlertMessage,
                                    dialogNeutralBtnLabel = data.biometricsTranslations.biometricsAlertNeutralBtn,
                                    dialogPositiveBtnLabel = data.biometricsTranslations.biometricsAlertPositiveBtn,
                                    dialogNegativeBtnLabel = data.biometricsTranslations.biometricsAlertNegativeBtn,
                                    biometricToastMessage = data.biometricsTranslations.biometricsToastMessage,
                                    biometricErrorSetupRequired = data.biometricsTranslations.biometricsErrorSetupRequired,
                                    biometricErrorCancelled = data.biometricsTranslations.biometricsErrorCanceled,
                                    biometricErrorDefault = data.biometricsTranslations.biometricsErrorDefault,
                                    biometricErrorLockout = data.biometricsTranslations.biometricsErrorLockout,
                                    biometricNoneEnrolled = data.biometricsTranslations.biometricsErrorNoneEnrolled
                                ),
                                secondStageTranslations = SecondStageTranslations(
                                    pinHeaderCreatePin = data.pinLockTranslations.pinCreate,
                                    pinHeaderConfirmPin = data.pinLockTranslations.pinConfirm,
                                    pinHeaderEnterPin = data.pinLockTranslations.pinEnter,
                                    pinHeaderIncorrectPin = data.pinLockTranslations.pinIncorrect,
                                    pinHeaderIncorrectPinCreateNew = data.pinLockTranslations.pinIncorrectNew,
                                    pinFooterCancelSettings = data.pinLockTranslations.pinForgotButtonCancel,
                                    pinFooterRemainingAttempts = data.pinLockTranslations.pinAttemptsText
                                ),
                                downloadTranslations = DownloadTranslations(
                                    displayMessage = data.generalMessages.download,
                                ),
                                minimumOSTranslations = MinimumOSTranslations(
                                    dialogTitle = "${data.osVersionSettings.minOsVersionTitle} " +
                                            "Android ${data.featureSettings.minOsVersionAndroid}",
                                    dialogMessage = data.osVersionSettings.minOsVersionMessage,
                                    confirmBtn = data.generalMessages.ok,
                                    dismissBtn = data.osVersionSettings.minOsVersionInstruction
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
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            shouldDisplayTutorial = false,
                            canDisplayTutorialScreen = false
                        )
                    }

                    cacheAppConfig.invoke(
                        AppConfigModel(
                            isTutorialDisplayed = true
                        )
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
                        isInitializeErrorDialogVisible = false
                    )
                }
            }
        }
    }

    private fun displayBiometricFailedDialog() {
        _uiState.update {
            it.copy(
                isBiometricsErrorDialogVisible = true,
                webViewScreenUiState = it.webViewScreenUiState.copy(
                    customScript = HIDE_BIOMETRICS_SCRIPT
                ),
                viewTranslations = it.viewTranslations.copy(
                    biometricsTranslations = it.viewTranslations.biometricsTranslations.copy(
                        currentDialogTitle = "Error",
                        currentDialogMessage = it.viewTranslations.biometricsTranslations.biometricErrorSetupRequired,
                        currentDialogButtonLabel = it.viewTranslations.biometricsTranslations.dialogNeutralBtnLabel
                    )
                )
            )
        }
    }

    private fun displayBiometricNoneEnrolled() {
        _uiState.update {
            it.copy(
                isBiometricsErrorDialogVisible = true,
                webViewScreenUiState = it.webViewScreenUiState.copy(
                    customScript = HIDE_BIOMETRICS_SCRIPT
                ),
                viewTranslations = it.viewTranslations.copy(
                    biometricsTranslations = it.viewTranslations.biometricsTranslations.copy(
                        currentDialogTitle = it.viewTranslations.biometricsTranslations.biometricNoneEnrolled,
                        currentDialogMessage = it.viewTranslations.biometricsTranslations.biometricErrorSetupRequired,
                        currentDialogButtonLabel = it.viewTranslations.biometricsTranslations.dialogNeutralBtnLabel
                    )
                )
            )
        }
    }

    private fun initHelperScreenUrl(url: String): String {
        val baseUrl = uiState.value.webViewScreenUiState.webviewUrl?.removeSuffix("/")
        val endpoint = url.removeSurrounding("\"")
        return if (endpoint.contains("http")) endpoint else "$baseUrl$endpoint"
    }

    private fun initCallbackUrl(url: String): String {
        val baseUrl = uiState.value.webViewScreenUiState.webviewUrl
        val endpoint = if (url.startsWith("/").not()) "/$url" else url
        return "$baseUrl$endpoint"
    }

    private fun isHttpOrSocial(url: String): Boolean {
        return (url.startsWith("http") || (url.contains("viber:") ||
                url.contains("tel::") ||
                url.contains("mailto:")))
    }
}