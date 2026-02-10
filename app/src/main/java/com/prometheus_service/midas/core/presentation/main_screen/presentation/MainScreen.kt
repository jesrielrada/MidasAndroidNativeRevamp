package com.prometheus_service.midas.core.presentation.main_screen.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.GameScreen
import com.prometheus_service.midas.core.presentation.features.language_selection.presentation.LanguageSelectionScreen
import com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.SplashScreen
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.TutorialScreen
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.WebviewScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.*
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenSideEffect
import com.prometheus_service.midas.core.presentation.util.GoogleAuthManager
import timber.log.Timber

@Composable
fun LockScreenOrientation(orientation: Int, context: Context) {
    DisposableEffect(orientation) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    activity: FragmentActivity
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val googleAuthManager = remember { GoogleAuthManager(context) }

    val shouldDisplayWebview = uiState.shouldDisplayWebview
    val shouldDisplayGameView = uiState.shouldDisplayGameView
    val webviewVisibility = if (shouldDisplayWebview) 1f else 0f

    val shouldRestartSplash by remember {
        derivedStateOf {
            uiState.isErrorDialogVisible
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                MainScreenSideEffect.DisplayBiometricSuccessEnrollment -> {
                    Toast.makeText(context, "Biometrics login enabled ", Toast.LENGTH_SHORT).show()
                }

                is MainScreenSideEffect.DisplayBiometricPrompt -> {
                    Timber.d("Displaying biometric prompt...")
                    val authCallback = object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            val isFromAccountSelection = false
                            if (isFromAccountSelection) {
                                Timber.d("Biometric authentication succeeded, is from account selection .. ")
                            } else {
                                Timber.d("Biometric authentication succeeded, is not from account selection .. ")
                                viewModel.onEvent(HandleBiometricsAuthResult(result))
                            }
                        }
                    }
                    val prompt = BiometricPrompt(activity, authCallback)
                    val info = BiometricPrompt.PromptInfo.Builder().apply {
                        setTitle("Biometrics Sign in")
                        setConfirmationRequired(false)
                        setNegativeButtonText("CANCEL")
                    }.build()

                    val crypto = BiometricPrompt.CryptoObject(effect.cipher)
                    prompt.authenticate(info, crypto)
                }

                is MainScreenSideEffect.OnStoreCredentials -> {
                    Timber.d("Store credentials called on side effects, calling handle store credentials...")
                    viewModel.onEvent(HandleStoreCredentials(effect.data))
                }

                is MainScreenSideEffect.OnPwaReady -> {
                    Timber.d("PWA ready called on side effects, calling handle pwa ...")
                    viewModel.onEvent(HandlePwaReady(effect.data))
                }

                is MainScreenSideEffect.ClearGoogleCredential -> {
                    googleAuthManager.clearSession()
                }

                is MainScreenSideEffect.RequestGoogleLogin -> {
                    try {
                        val clientId = viewModel.googleClientId
                        val result = googleAuthManager.getGoogleCredential(clientId)

                        viewModel.onEvent(
                            ProcessGoogleLogin(
                                clientId = clientId,
                                response = result,
                                url = effect.url,
                            )
                        )
                    } catch (e: Exception) {
                        Timber.e("Creating credential manager failed... $e")
                    }
                }

                MainScreenSideEffect.DisplayBiometricsEnableDialog -> {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("Enabled biometric authentication")
                        .setMessage("Use your biometric on your next sign in")
                        .setPositiveButton("Enable") { _, _ ->
                            Timber.tag("BiometricsPrompt").d("Enable")
                            viewModel.onEvent(InitializeBiometricPrompt)
                        }
                        .setNeutralButton("Later") { _, _ ->
                            Timber.tag("BiometricsPrompt").d("Not Now")
                        }
                        .setNegativeButton("Dont show again") { _, _ ->
                            // do nothing, hide prompt
                            Timber.tag("BiometricsPrompt").d("Dont show again")

                        }.show()
                }
            }
        }
    }

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, context)

    Box(modifier = Modifier.fillMaxSize()) {
        WebviewScreen(
            modifier = Modifier.alpha(webviewVisibility),
            uiState = uiState.webViewScreenUiState,
            onWebviewInitialized = {
                viewModel.onEvent(SetUserAgentReady(it))
            },
            onRouteLoaded = {
                viewModel.onEvent(ResetCustomRoute)
                viewModel.onEvent(HideGameViewScreen)
            },
            onUrlLoaded = {
                viewModel.onEvent(SetWebviewUrlLoaded)
            },
            onCustomUrlLoaded = {
                viewModel.onEvent(ResetCustomUrl)
            },
            onPwaReady = { data ->
                viewModel.emitSideEffect(MainScreenSideEffect.OnPwaReady(data))
            },
            onPwaNavigate = { route ->
                route?.let {
                    viewModel.onEvent(UpdateCurrentRoute(it))
                }
            },
            onStoreCredentials = { data ->
                viewModel.emitSideEffect(MainScreenSideEffect.OnStoreCredentials(data))
            },
            onNewGameLauncher = { path ->
                viewModel.onEvent(MainScreenEvent.LaunchGamePage(gamePath = path))
            },
            onNativeAuthenticateGoogle = {
                viewModel.emitSideEffect(MainScreenSideEffect.ClearGoogleCredential)
            },
            onNativeLaunchGoogle = {
                viewModel.emitSideEffect(MainScreenSideEffect.RequestGoogleLogin(it))
            }
        )

        if (shouldDisplayGameView) {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, context)
            GameScreen(
                gameUrl = uiState.gameUrl,
                gameScreenTranslations = uiState.viewTranslations.gameScreenTranslations,
                onReturnDialogConfirm = {
                    viewModel.onEvent(HideGameViewScreen)
                },
                onHomeButtonClicked = {
                    val route = "javascript: window.pwa.navigate({ name: 'dashboard-route'})"
                    viewModel.onEvent(LoadCustomRoute(route))
                },
                onDepositButtonClicked = {
                    viewModel.onEvent(LoadCustomRoute(it))
                }
            )
        }

        if (uiState.shouldDisplayTutorial) {
            TutorialScreen(
                tutorialTranslations = uiState.viewTranslations.tutorialScreenTranslations,
                onTutorialFinished = {
                    viewModel.onEvent(HideTutorialScreen)
                }
            )
        }

        if (uiState.shouldDisplaySplash) {
            LaunchedEffect(uiState.webViewScreenUiState.isPwaReady) {
                if (uiState.webViewScreenUiState.isPwaReady && uiState.isAppInitialized) {
                    if (uiState.webViewScreenUiState.isPwaReady) {
                        viewModel.onEvent(HideSplashScreen)
                    }
                    if (uiState.canDisplayTutorialScreen) {
                        viewModel.onEvent(DisplayTutorialScreen)
                    }
                }
            }

            SplashScreen(
                splashScreenTranslations = uiState.viewTranslations.splashScreenTranslations,
                onClickSkipBtn = {
                    if (uiState.webViewScreenUiState.isPwaReady && uiState.isAppInitialized) {
                        viewModel.onEvent(HideSplashScreen)
                    }
                    if (uiState.canDisplayTutorialScreen) {
                        viewModel.onEvent(DisplayTutorialScreen)
                    }
                },
                onScrollFinished = {
                    Timber.d("Scroll finished called")
                    if (uiState.webViewScreenUiState.isPwaReady && uiState.isAppInitialized) {
                        viewModel.onEvent(HideSplashScreen)
                    }
                    if (uiState.canDisplayTutorialScreen) {
                        viewModel.onEvent(DisplayTutorialScreen)
                    }
                },
                shouldDisplaySkipButton = uiState.webViewScreenUiState.isPwaReady,
                shouldRestartSplash = shouldRestartSplash
            )
        }

        if (uiState.shouldDisplayLanguageSelection) {
            LanguageSelectionScreen(
                onLanguageSelected = { locale ->
                    viewModel.onEvent(HideLanguageSelectionScreen)
                    viewModel.onEvent(SetLocaleSelected(locale))
                }
            )
        }

        if (uiState.isErrorDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    //do nothing
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onEvent(DismissErrorDialog)
                        viewModel.onEvent(InitializeApplication)
                    }) {
                        Text(
                            color = Color.White,
                            text = uiState.viewTranslations.mainScreenTranslations.retryButtonLabel
                        )
                    }
                },
                text = {
                    Text(
                        color = Color.White,
                        text = uiState.viewTranslations.mainScreenTranslations.initializeErrorMessage
                    )
                }
            )
        }
    }
}