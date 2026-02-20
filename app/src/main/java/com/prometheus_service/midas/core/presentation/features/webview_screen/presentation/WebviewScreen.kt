package com.prometheus_service.midas.core.presentation.features.webview_screen.presentation

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.prometheus_service.midas.core.presentation.features.webview_screen.clients.chrome.DefaultWebChromeClient
import com.prometheus_service.midas.core.presentation.features.webview_screen.clients.webview.DefaultWebviewClient
import com.prometheus_service.midas.core.presentation.features.webview_screen.javascript.DefaultJavascriptListener
import com.prometheus_service.midas.core.presentation.features.webview_screen.javascript.JavascriptListener
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.DisplayBiometricAccountSelection
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleBiometricsLogin
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleCustomScriptCallback
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleMemberLoggedOut
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandlePinCodeToggled
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleSwitchLanguage
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HideGameViewScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.ResetCustomRoute
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.ResetCustomUrl
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.SetUserAgentReady
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.SetWebviewUrlLoaded
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.UpdateCurrentRoute
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenSideEffect
import com.prometheus_service.midas.core.presentation.main_screen.presentation.MainScreenViewModel
import timber.log.Timber

@Composable
fun WebviewScreen(
    modifier: Modifier = Modifier,
    uiState: WebViewScreenUiState,
    viewModel: MainScreenViewModel
) {
    WebviewScreenContent(
        modifier = modifier,
        uiState = uiState,
        onInitialized = {
            viewModel.onEvent(SetUserAgentReady(it))
        },
        onPwaReady = {
            viewModel.emitSideEffect(MainScreenSideEffect.OnPwaReady(it))
        },
        onPwaNavigate = {
            it?.let {
                viewModel.onEvent(UpdateCurrentRoute(it))
            }
        },
        onStoreCredentials = {
            viewModel.emitSideEffect(MainScreenSideEffect.OnStoreCredentials(it))
        },
        onNewGameLauncher = {
            viewModel.onEvent(MainScreenEvent.LaunchGamePage(gamePath = it))
        },
        onRouteLoaded = {
            viewModel.onEvent(ResetCustomRoute)
            viewModel.onEvent(HideGameViewScreen)
        },
        onUrlLoaded = {
            viewModel.onEvent(SetWebviewUrlLoaded)
        },
        onNativeAuthenticateGoogle = {
            viewModel.emitSideEffect(MainScreenSideEffect.ClearGoogleCredential)
        },
        onNativeLaunchGoogle = {
            viewModel.emitSideEffect(MainScreenSideEffect.RequestGoogleLogin(it))
        },
        onCustomUrlLoaded = {
            viewModel.onEvent(ResetCustomUrl)
        },
        onShouldDisplayBiometricsLogin = {
            viewModel.onEvent(DisplayBiometricAccountSelection)
        },
        onLoginLauncher = {
            viewModel.onEvent(HandleBiometricsLogin)
        },
        onPincodeToggled = {
            viewModel.onEvent(HandlePinCodeToggled(it))
        },
        onCustomCallbackScriptLoaded = {
            viewModel.onEvent(HandleCustomScriptCallback(it))
        },
        onMemberLoggedIn = {
            viewModel.onEvent(MainScreenEvent.CacheSessionCookies)
        },
        onMemberLoggedOut = {
            viewModel.onEvent(HandleMemberLoggedOut)
        },
        onWebviewReloaded = {
            viewModel.updateMainState {
                it.copy(
                    webViewScreenUiState = it.webViewScreenUiState.copy(
                        shouldReloadWebview = false
                    )
                )
            }
        },
        onSwitchLanguage = {
            viewModel.onEvent(MainScreenEvent.CacheSessionCookies)
            viewModel.onEvent(HandleSwitchLanguage(it))
        },
        onOpenInBrowser = {
            viewModel.onEvent(MainScreenEvent.HandleOpenInBrowser(it))
        },
        onLaunchNewWindow = {
            viewModel.onEvent(MainScreenEvent.HandleLaunchNewWindow(it))
        },
        onMaintenanceMode = {
            viewModel.onEvent(MainScreenEvent.HandleMaintenanceMode)
        },
        onGeoBlockMode = {
            viewModel.onEvent(MainScreenEvent.HandleGeoBlockMode)
        },
        onThemeSetting = {

        },
        onRefreshCookie = {
            viewModel.onEvent(MainScreenEvent.CacheSessionCookies)
        }
    )
}


@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
@Composable
fun WebviewScreenContent(
    modifier: Modifier = Modifier,
    uiState: WebViewScreenUiState,
    onInitialized: (userAgent: String) -> Unit,
    onUrlLoaded: () -> Unit,
    onRouteLoaded: () -> Unit,
    onCustomUrlLoaded: () -> Unit,
    onPwaReady: (String) -> Unit,
    onPwaNavigate: (String?) -> Unit,
    onStoreCredentials: (String?) -> Unit,
    onNewGameLauncher: (String) -> Unit,
    onNativeAuthenticateGoogle: (String) -> Unit,
    onNativeLaunchGoogle: (String) -> Unit,
    onShouldDisplayBiometricsLogin: () -> Unit,
    onLoginLauncher: (String?) -> Unit,
    onPincodeToggled: (Boolean) -> Unit,
    onCustomCallbackScriptLoaded: (String) -> Unit,
    onMemberLoggedIn: (String) -> Unit,
    onMemberLoggedOut: (String) -> Unit,
    onWebviewReloaded: () -> Unit,
    onSwitchLanguage: (String) -> Unit,
    onOpenInBrowser: (String) -> Unit,
    onLaunchNewWindow: (String) -> Unit,
    onMaintenanceMode: (String?) -> Unit,
    onGeoBlockMode: (String?) -> Unit,
    onThemeSetting: (String) -> Unit,
    onRefreshCookie: (String) -> Unit
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.overScrollMode = View.OVER_SCROLL_NEVER
            this.settings.javaScriptEnabled = true
            this.settings.domStorageEnabled = true
            this.settings.allowFileAccess = true
            this.settings.allowContentAccess = true
            this.settings.javaScriptCanOpenWindowsAutomatically = true
            this.settings.setSupportMultipleWindows(true)

            this.settings.userAgentString = this.settings.userAgentString + " VN88MobileA/4.0.0"

            this.webViewClient = DefaultWebviewClient()
            this.webChromeClient = DefaultWebChromeClient(context)

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webviewJavascriptSetup(
                webView = this,
                onPwaReady = { onPwaReady(it) },
                onNewGameLauncher = { onNewGameLauncher(it) },
                onNativeAuthenticateGoogle = { onNativeAuthenticateGoogle(it) },
                onNativeLaunchGoogle = { onNativeLaunchGoogle(it) },
                onStoreCredentials = { onStoreCredentials(it) },
                onPwaNavigate = { onPwaNavigate(it) },
                onShouldDisplayBiometricsLogin = { onShouldDisplayBiometricsLogin() },
                onLoginLauncher = { onLoginLauncher(it) },
                onPincodeToggled = { onPincodeToggled(it) },
                onMemberLoggedIn = { onMemberLoggedIn(it) },
                onMemberLoggedOut = { onMemberLoggedOut(it) },
                onSwitchLanguage = { onSwitchLanguage(it) },
                onOpenInBrowser = { onOpenInBrowser(it) },
                onLaunchNewWindow = { onLaunchNewWindow(it) },
                onMaintenanceMode = { onMaintenanceMode(it) },
                onGeoBlockMode = { onGeoBlockMode(it) },
                onThemeSetting = { onThemeSetting(it) },
                onRefreshCookie = { onRefreshCookie(it) }
            )
            onInitialized(this.settings.userAgentString)
        }
    }

    LaunchedEffect(uiState.webviewUrl) {
        val targetUrl = uiState.webviewUrl
        if (targetUrl != null && !uiState.isWebViewUrlLoaded) {
            Timber.d("Loading webview url ... $targetUrl")
            webView.loadUrl(targetUrl)
            onUrlLoaded()
        }
    }

    LaunchedEffect(uiState.customRoute) {
        val targetRoute = uiState.customRoute
        if (targetRoute != null) {
            Timber.d("Loading custom route ... $targetRoute")
            webView.loadUrl(targetRoute)
            onRouteLoaded()
        }

    }
    LaunchedEffect(uiState.customUrl) {
        if (uiState.customUrl != null) {
            Timber.d("Loading custom url ... ${uiState.customUrl}")
            webView.loadUrl(uiState.customUrl)
            onCustomUrlLoaded()
        }
    }

    LaunchedEffect(uiState.customScript) {
        if (uiState.customScript != null) {
            Timber.d("Loading custom script ... ${uiState.customScript}")
            webView.loadUrl(uiState.customScript)
            onCustomUrlLoaded()
        }
    }

    LaunchedEffect(uiState.customCallbackScript) {
        if (uiState.customCallbackScript != null) {
            Timber.d("Loading custom callback script ... ${uiState.customCallbackScript}")
            webView.evaluateJavascript(uiState.customCallbackScript) {
                onCustomCallbackScriptLoaded(it)
            }
        }
    }

    LaunchedEffect(uiState.shouldReloadWebview) {
        if (uiState.shouldReloadWebview) {
            Timber.d("Reloading webview ... ${uiState.webviewUrl}")
            webView.loadUrl(uiState.webviewUrl!!)
            onWebviewReloaded()
        }
    }

    DisposableEffect(webView) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    Scaffold(modifier = modifier) { innerPadding ->
        AndroidView(
            factory = {
                webView
            },
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}


fun webviewJavascriptSetup(
    webView: WebView,
    onPwaReady: (data: String) -> Unit,
    onNewGameLauncher: (url: String) -> Unit,
    onNativeAuthenticateGoogle: (data: String) -> Unit,
    onNativeLaunchGoogle: (url: String) -> Unit,
    onStoreCredentials: (data: String?) -> Unit,
    onPwaNavigate: (route: String?) -> Unit,
    onShouldDisplayBiometricsLogin: () -> Unit,
    onLoginLauncher: (data: String?) -> Unit,
    onPincodeToggled: (isEnabled: Boolean) -> Unit,
    onMemberLoggedIn: (data: String) -> Unit,
    onMemberLoggedOut: (data: String) -> Unit,
    onSwitchLanguage: (language: String) -> Unit,
    onOpenInBrowser: (url: String) -> Unit,
    onLaunchNewWindow: (url: String) -> Unit,
    onMaintenanceMode: (data: String?) -> Unit,
    onGeoBlockMode: (data: String?) -> Unit,
    onThemeSetting: (data: String) -> Unit,
    onRefreshCookie: (data: String) -> Unit
) {
    webView.addJavascriptInterface(
        DefaultJavascriptListener(
            object : JavascriptListener {
                override fun onPwaReady(data: String) {
                    onPwaReady(data)
                }

                override fun onNewGameLauncher(url: String) {
                    onNewGameLauncher(url)
                }

                override fun onPwaNavigate(route: String?) {
                    onPwaNavigate(route)
                }

                override fun onNativeAuthenticateGoogle(data: String) {
                    onNativeAuthenticateGoogle(data)
                }

                override fun onNativeLaunchGoogle(url: String) {
                    onNativeLaunchGoogle(url)
                }

                override fun onStoreCredentials(data: String?) {
                    onStoreCredentials(data)
                }

                override fun onResetCredentials(data: String?) {
                    //
                }

                override fun onShouldDisplayBiometricsLogin(enabled: Boolean) {
                    onShouldDisplayBiometricsLogin()
                }

                override fun onLoginLauncher(data: String?) {
                    onLoginLauncher(data)
                }

                override fun onPinCodeToggle(isEnabled: Boolean) {
                    onPincodeToggled(isEnabled)
                }

                override fun onMemberLoggedIn(data: String) {
                    onMemberLoggedIn(data)
                }

                override fun onMemberLoggedOut(data: String) {
                    onMemberLoggedOut(data)
                }

                override fun onSwitchLanguage(language: String) {
                    onSwitchLanguage(language)
                }

                override fun onOpenInBrowser(url: String) {
                    onOpenInBrowser(url)
                }

                override fun onLaunchNewWindow(url: String) {
                    onLaunchNewWindow(url)
                }

                override fun onMaintenanceMode(data: String?) {
                    onMaintenanceMode(data)
                }

                override fun onGeoBlockMode(data: String?) {
                    onGeoBlockMode(data)
                }

                override fun onThemeSetting(data: String) {
                    onThemeSetting(data)
                }

                override fun onRefreshCookie(data: String) {
                    onRefreshCookie(data)
                }
            }
        ),
        "Android"
    )
}