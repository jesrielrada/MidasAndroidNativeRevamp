package com.prometheus_service.midas.core.presentation.main_screen.presentation

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import timber.log.Timber

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // Restore original orientation when the composable leaves the composition
            activity.requestedOrientation = originalOrientation
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val shouldDisplayWebview = uiState.shouldDisplayWebview
    val shouldDisplayGameView = uiState.shouldDisplayGameView
    val webviewVisibility = if (shouldDisplayWebview) 1f else 0f

    val shouldRestartSplash by remember {
        derivedStateOf {
            uiState.isErrorDialogVisible
        }
    }

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Box(modifier = Modifier.fillMaxSize()) {
        WebviewScreen(
            modifier = Modifier.alpha(webviewVisibility),
            onPageFinished = {
                viewModel.onEvent(MainScreenEvent.OnWebviewReady)
            }
        )

        if (shouldDisplayGameView) {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            GameScreen(
                gameScreenTranslations = uiState.viewTranslations.gameScreenTranslations,
                onReturnDialogConfirm = {
                    viewModel.onEvent(MainScreenEvent.HideGameViewScreen)
                }
            )
        }

        if (uiState.shouldDisplayTutorial) {
            TutorialScreen(
                tutorialTranslations = uiState.viewTranslations.tutorialScreenTranslations,
                onInitialized = { canDisplay ->
                    if (!canDisplay) {
                        viewModel.onEvent(MainScreenEvent.HideTutorialScreen)
                        viewModel.onEvent(MainScreenEvent.DisplayWebviewScreen)
                    }
                },
                onTutorialFinished = {
                    viewModel.onEvent(MainScreenEvent.HideTutorialScreen)
                    viewModel.onEvent(MainScreenEvent.DisplayWebviewScreen)
                }
            )
        }

        if (uiState.shouldDisplayLanguageSelection) {
            LanguageSelectionScreen(
                onInitialized = { canDisplay ->
                    Timber.d("Language selection initialized.. canDisplay: $canDisplay")
                    if (!canDisplay) {
                        viewModel.onEvent(MainScreenEvent.HideLanguageSelectionScreen)
                        viewModel.onEvent(MainScreenEvent.DisplayWebviewScreen)
                    }
                },
                onLanguageSelected = { locale ->
                    viewModel.onEvent(MainScreenEvent.HideLanguageSelectionScreen)
                    viewModel.onEvent(MainScreenEvent.DisplayWebviewScreen)
                    viewModel.onEvent(MainScreenEvent.SyncRemoteData(locale))
                }
            )
        }

        if (uiState.shouldDisplaySplash) {
            SplashScreen(
                splashScreenTranslations = uiState.viewTranslations.splashScreenTranslations,
                onClickSkipBtn = {
                    if (uiState.isWebviewReady && uiState.isAppInitialized) {
                        viewModel.onEvent(MainScreenEvent.HideSplashScreen)
                    }
                },
                onScrollFinished = {
                    Timber.d("Scroll finished called")
                    if (uiState.isWebviewReady && uiState.isAppInitialized) {
                        viewModel.onEvent(MainScreenEvent.HideSplashScreen)
                    }
                },
                isReadyToHide = uiState.isWebviewReady && uiState.isAppInitialized,
                shouldRestartSplash = shouldRestartSplash
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