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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.prometheus_service.midas.core.presentation.features.webview_screen.clients.chrome.DefaultWebChromeClient
import com.prometheus_service.midas.core.presentation.features.webview_screen.clients.webview.DefaultWebviewClient
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
    modifier: Modifier = Modifier, uiState: WebViewScreenUiState, viewModel: MainScreenViewModel
) {
    WebviewScreenContent(modifier = modifier, uiState = uiState, onInitialized = {
        viewModel.onEvent(SetUserAgentReady(it))
    }, onPwaReady = {
        viewModel.emitSideEffect(MainScreenSideEffect.OnPwaReady(it))
    }, onPwaNavigate = {
        it?.let {
            viewModel.onEvent(UpdateCurrentRoute(it))
        }
    }, onNewGameLauncher = {
        viewModel.onEvent(MainScreenEvent.LaunchGamePage(gamePath = it))
    }, onRouteLoaded = {
        viewModel.onEvent(ResetCustomRoute)
        viewModel.onEvent(HideGameViewScreen)
    }, onUrlLoaded = {
        viewModel.onEvent(SetWebviewUrlLoaded)
    }, onNativeAuthenticateGoogle = {
        viewModel.emitSideEffect(MainScreenSideEffect.ClearGoogleCredential)
    }, onNativeLaunchGoogle = {
        viewModel.emitSideEffect(MainScreenSideEffect.RequestGoogleLogin(it))
    }, onCustomUrlLoaded = {
        viewModel.onEvent(ResetCustomUrl)
    }, onShouldDisplayBiometricsLogin = {
        viewModel.onEvent(DisplayBiometricAccountSelection)
    }, onLoginLauncher = {
        viewModel.onEvent(HandleBiometricsLogin)
    }, onPincodeToggled = {
        viewModel.onEvent(HandlePinCodeToggled(it))
    }, onCustomCallbackScriptLoaded = {
        viewModel.onEvent(HandleCustomScriptCallback(it))
    }, onMemberLoggedIn = {
        viewModel.onEvent(MainScreenEvent.MemberLoggedIn(it))
    }, onMemberLoggedOut = {
        viewModel.onEvent(HandleMemberLoggedOut)
    }, onWebviewReloaded = {
        viewModel.updateMainState {
            it.copy(
                webViewScreenUiState = it.webViewScreenUiState.copy(
                    shouldReloadWebview = false
                )
            )
        }
    }, onSwitchLanguage = {
        viewModel.onEvent(MainScreenEvent.CacheSessionCookies)
        viewModel.onEvent(HandleSwitchLanguage(it))
    }, onOpenInBrowser = {
        viewModel.onEvent(MainScreenEvent.HandleOpenInBrowser(it))
    }, onLaunchNewWindow = {
        viewModel.onEvent(MainScreenEvent.HandleLaunchNewWindow(it))
    }, onMaintenanceMode = {
        viewModel.onEvent(MainScreenEvent.HandleMaintenanceMode)
    }, onGeoBlockMode = {
        viewModel.onEvent(MainScreenEvent.HandleGeoBlockMode)
    }, onRefreshCookie = {
        viewModel.onEvent(MainScreenEvent.CacheSessionCookies)
    }, onResetCredentials = {
        viewModel.onEvent(MainScreenEvent.HandleResetCredentials(it))
    }, onShouldDisplaySecondStage = {
        viewModel.onEvent(MainScreenEvent.HandleShouldDisplaySecondStage(it))
    })
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
    onRefreshCookie: (String) -> Unit,
    onResetCredentials: (String) -> Unit,
    onShouldDisplaySecondStage: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
            overScrollMode = View.OVER_SCROLL_NEVER
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)
                userAgentString = settings.userAgentString + " VN88MobileA/4.0.0"
            }
            webViewClient = DefaultWebviewClient()
            webChromeClient = DefaultWebChromeClient(context)

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

            webviewJavascriptSetup(
                webView = this,
                onPwaReady = onPwaReady,
                onNewGameLauncher = onNewGameLauncher,
                onNativeAuthenticateGoogle = onNativeAuthenticateGoogle,
                onNativeLaunchGoogle = onNativeLaunchGoogle,
                onPwaNavigate = onPwaNavigate,
                onShouldDisplayBiometricsLogin = onShouldDisplayBiometricsLogin,
                onLoginLauncher = onLoginLauncher,
                onPincodeToggled = onPincodeToggled,
                onMemberLoggedIn = onMemberLoggedIn,
                onMemberLoggedOut = onMemberLoggedOut,
                onSwitchLanguage = onSwitchLanguage,
                onOpenInBrowser = onOpenInBrowser,
                onLaunchNewWindow = onLaunchNewWindow,
                onMaintenanceMode = onMaintenanceMode,
                onGeoBlockMode = onGeoBlockMode,
                onRefreshCookie = onRefreshCookie,
                onResetCredentials = onResetCredentials,
                onShouldDisplaySecondStage = onShouldDisplaySecondStage
            )


            onInitialized(this.settings.userAgentString)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    webView.stopLoading()
                    webView.destroy()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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

    Scaffold(modifier = modifier) { innerPadding ->
        AndroidView(
            factory = { webView }, modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}
