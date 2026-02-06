package com.prometheus_service.midas.core.presentation.main_screen.presentation

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.GameScreen
import com.prometheus_service.midas.core.presentation.features.language_selection.presentation.LanguageSelectionScreen
import com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.SplashScreen
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.TutorialScreen
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.WebviewScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
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
    viewModel: MainScreenViewModel = hiltViewModel()
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
                MainScreenSideEffect.ClearGoogleCredential -> {
                    googleAuthManager.clearSession()
                }

                is MainScreenSideEffect.RequestGoogleLogin -> {
                    try {
                        val clientId = viewModel.googleClientId
                        val result = googleAuthManager.getGoogleCredential(clientId)

                        viewModel.onEvent(
                            MainScreenEvent.ProcessGoogleLogin(
                                clientId = clientId,
                                response = result,
                                url = effect.url,
                            )
                        )
                    } catch (e: Exception) {
                        Timber.e("Creating credential manager failed... $e")
                    }
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
                viewModel.onEvent(MainScreenEvent.SetUserAgentReady(it))
            },
            onRouteLoaded = {
                viewModel.onEvent(MainScreenEvent.ResetCustomRoute)
                viewModel.onEvent(MainScreenEvent.HideGameViewScreen)
            },
            onUrlLoaded = {
                viewModel.onEvent(MainScreenEvent.SetWebviewUrlLoaded)
            },
            onCustomUrlLoaded = {
                viewModel.onEvent(MainScreenEvent.ResetCustomUrl)
            },
            onPwaReady = {
                viewModel.onEvent(MainScreenEvent.OnWebviewReady)
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
                    viewModel.onEvent(MainScreenEvent.HideGameViewScreen)
                },
                onHomeButtonClicked = {
                    val route = "javascript: window.pwa.navigate({ name: 'dashboard-route'})"
                    viewModel.onEvent(MainScreenEvent.LoadCustomRoute(route))
                },
                onDepositButtonClicked = {
                    viewModel.onEvent(MainScreenEvent.LoadCustomRoute(it))
                }
            )
        }

        if (uiState.shouldDisplayTutorial) {
            TutorialScreen(
                tutorialTranslations = uiState.viewTranslations.tutorialScreenTranslations,
                onTutorialFinished = {
                    viewModel.onEvent(MainScreenEvent.HideTutorialScreen)
                }
            )
        }

        if (uiState.shouldDisplaySplash) {
            LaunchedEffect(uiState.webViewScreenUiState.isWebviewReady) {
                if (uiState.webViewScreenUiState.isWebviewReady && uiState.isAppInitialized) {
                    if (uiState.webViewScreenUiState.isWebviewReady) {
                        viewModel.onEvent(MainScreenEvent.HideSplashScreen)
                    }
                    if (uiState.canDisplayTutorialScreen) {
                        viewModel.onEvent(MainScreenEvent.DisplayTutorialScreen)
                    }
                }
            }

            SplashScreen(
                splashScreenTranslations = uiState.viewTranslations.splashScreenTranslations,
                onClickSkipBtn = {
                    if (uiState.webViewScreenUiState.isWebviewReady && uiState.isAppInitialized) {
                        viewModel.onEvent(MainScreenEvent.HideSplashScreen)
                    }
                    if (uiState.canDisplayTutorialScreen) {
                        viewModel.onEvent(MainScreenEvent.DisplayTutorialScreen)
                    }
                },
                onScrollFinished = {
                    Timber.d("Scroll finished called")
                    if (uiState.webViewScreenUiState.isWebviewReady && uiState.isAppInitialized) {
                        viewModel.onEvent(MainScreenEvent.HideSplashScreen)
                    }
                    if (uiState.canDisplayTutorialScreen) {
                        viewModel.onEvent(MainScreenEvent.DisplayTutorialScreen)
                    }
                },
                shouldDisplaySkipButton = uiState.webViewScreenUiState.isWebviewReady,
                shouldRestartSplash = shouldRestartSplash
            )
        }

        if (uiState.shouldDisplayLanguageSelection) {
            LanguageSelectionScreen(
                onLanguageSelected = { locale ->
                    viewModel.onEvent(MainScreenEvent.HideLanguageSelectionScreen)
                    viewModel.onEvent(MainScreenEvent.SetLocaleSelected(locale))
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
                        viewModel.onEvent(MainScreenEvent.DismissErrorDialog)
                        viewModel.onEvent(MainScreenEvent.InitializeApplication)
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