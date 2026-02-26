package com.prometheus_service.midas.core.presentation.main_screen.presentation

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prometheus_service.midas.core.presentation.features.biometrics.BiometricAuthenticator
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.GameScreen
import com.prometheus_service.midas.core.presentation.features.helper_screen.presentation.HelperScreen
import com.prometheus_service.midas.core.presentation.features.language_selection.presentation.LanguageSelectionScreen
import com.prometheus_service.midas.core.presentation.features.second_stage.presentation.SecondStageScreen
import com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.SplashScreen
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.TutorialScreen
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.WebviewScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.DismissErrorDialog
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.DisplayTutorialScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandlePinCodeToggleOff
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleSecondStageMaxAttempt
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HideBiometricEnableDialog
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HideBiometricErrorDialog
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HideGameViewScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HideLanguageSelectionScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HideSplashScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HideTutorialScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.InitializeApplication
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.InitializeBiometricPrompt
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.InitializeTranslations
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.LoadBaseUrl
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.LoadCustomRoute
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.SetBiometricsDisabled
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.SetLocaleSelected
import com.prometheus_service.midas.core.presentation.main_screen.presentation.dialogs.BiometricsEnableDialog
import com.prometheus_service.midas.core.presentation.main_screen.presentation.dialogs.BiometricsErrorDialog
import com.prometheus_service.midas.core.presentation.main_screen.presentation.dialogs.BiometricsLoadingDialog
import com.prometheus_service.midas.core.presentation.main_screen.presentation.dialogs.DefaultErrorDialog
import com.prometheus_service.midas.core.presentation.main_screen.presentation.dialogs.MinimumOsVersionDialog
import com.prometheus_service.midas.core.presentation.main_screen.presentation.dialogs.NetworkErrorDialog
import com.prometheus_service.midas.core.presentation.main_screen.presentation.handler.MainScreenEffectHandler
import com.prometheus_service.midas.core.presentation.main_screen.presentation.handler.MainScreenLifecycleHandler
import com.prometheus_service.midas.core.presentation.main_screen.presentation.handler.MainScreenOrientationHandler
import com.prometheus_service.midas.core.presentation.util.GoogleAuthManager
import kotlinx.coroutines.launch
import timber.log.Timber


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(),
    activity: FragmentActivity
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val googleAuthManager = remember { GoogleAuthManager(context) }
    val authenticator = remember(activity) { BiometricAuthenticator(activity) }

    val shouldDisplayWebview = uiState.shouldDisplayWebview
    val shouldDisplayGameView = uiState.shouldDisplayGameView
    val webviewVisibility = if (shouldDisplayWebview) 1f else 0f

    val shouldRestartSplash by remember { derivedStateOf { uiState.isInitializeErrorDialogVisible } }
    val snackBarHostState = remember { SnackbarHostState() }

    // Define the desired orientation based on current state
    val targetOrientation = remember(uiState.currentRoute) {
        when {
            // Specific route allows landscape/sensor rotation
            uiState.currentRoute == "game-launcher-route" -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            // Default app state
            else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }


    LaunchedEffect(uiState.onDataSync) {
        if (uiState.onDataSync) {
            Timber.d("Data sync complete, initializing translations ...")
            viewModel.onEvent(InitializeTranslations)
            viewModel.updateMainState {
                it.copy(
                    onDataSync = false
                )
            }
        }
    }

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            snackBarHostState.showSnackbar(message)
            viewModel.updateMainState {
                it.copy(
                    snackBarMessage = null
                )
            }
        }
    }

    MainScreenEffectHandler(
        viewModel = viewModel,
        context = context,
        authenticator = authenticator,
        googleAuthManager = googleAuthManager
    )

    MainScreenLifecycleHandler(viewModel = viewModel)

    MainScreenOrientationHandler(targetOrientation, context)

    Box(modifier = Modifier.fillMaxSize()) {
        WebviewScreen(
            modifier = Modifier.alpha(webviewVisibility),
            uiState = uiState.webViewScreenUiState,
            viewModel = viewModel
        )

        if (shouldDisplayGameView) {
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
                    viewModel.onEvent(LoadBaseUrl)
                }
            )
        }

        if (uiState.shouldDisplaySecondStage) {
            SecondStageScreen(
                translations = uiState.viewTranslations.secondStageTranslations,
                onCancel = {
                    viewModel.onEvent(HandlePinCodeToggleOff)
                },
                onHideScreen = {
                    viewModel.updateMainState {
                        it.copy(
                            shouldDisplaySecondStage = false
                        )
                    }
                },
                onMaxAttempt = {
                    Timber.d("Max attempt called")
                    viewModel.onEvent(HandleSecondStageMaxAttempt)
                }
            )
        }

        if (uiState.shouldDisplayHelperScreen) {
            HelperScreen(
                uiState = uiState.helperUiState,
                onHideScreen = {
                    viewModel.updateMainState {
                        it.copy(
                            shouldDisplayHelperScreen = false,
                            helperUiState = it.helperUiState.copy(
                                url = null,
                                displayMessage = null
                            )
                        )
                    }
                }, onDownloadProcessed = {
                    val message = uiState.viewTranslations
                        .downloadTranslations
                        .displayMessage + " $it"

                    Timber.d("Download processed ... $message")

                    viewModel.updateMainState { state ->
                        state.copy(
                            helperUiState = state.helperUiState.copy(
                                displayMessage = message
                            )
                        )
                    }
                }
            )
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }

    if (uiState.isInitializeErrorDialogVisible) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    viewModel.onEvent(DismissErrorDialog)
                    viewModel.onEvent(InitializeApplication)
                }) {
                    Text(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        text = uiState.viewTranslations.mainScreenTranslations.retryButtonLabel
                    )
                }
            },
            text = {
                Text(
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    text = uiState.viewTranslations.mainScreenTranslations.initializeErrorMessage
                )
            }
        )
    }

    if (uiState.isBiometricsLoadingDialogVisible) {
        BiometricsLoadingDialog()
    }

    if (uiState.isBiometricsEnableDialogVisible) {
        BiometricsEnableDialog(
            translations = uiState.viewTranslations.biometricsTranslations,
            onConfirm = {
                viewModel.onEvent(InitializeBiometricPrompt)
                viewModel.onEvent(HideBiometricEnableDialog)
            },
            onDismiss = {
                viewModel.onEvent(HideBiometricEnableDialog)
            },
            onDontShowAgain = {
                viewModel.onEvent(SetBiometricsDisabled)
                viewModel.onEvent(HideBiometricEnableDialog)
            }
        )
    }

    if (uiState.isBiometricsErrorDialogVisible) {
        BiometricsErrorDialog(
            onConfirm = {
                viewModel.onEvent(SetBiometricsDisabled)
                viewModel.onEvent(HideBiometricErrorDialog)
            },
            onDismiss = {
                viewModel.onEvent(HideBiometricErrorDialog)
            },
            translations = uiState.viewTranslations.biometricsTranslations
        )
    }

    if (uiState.isDefaultErrorDialogVisible) {
        DefaultErrorDialog(
            onConfirm = {
                viewModel.updateMainState { it.copy(isDefaultErrorDialogVisible = false) }
            },
            dialogMessage = uiState.viewTranslations.defaultErrorTranslations.dialogMessage,
            dialogBtn = uiState.viewTranslations.defaultErrorTranslations.dialogBtn
        )
    }

    if (uiState.shouldDisplayNetworkError && uiState.shouldDisplaySplash.not()) {
        NetworkErrorDialog(
            onDismiss = { activity.finishAffinity() },
            uiState = uiState
        )
    }

    if (uiState.canDisplayMinimumOsDialog) {
        MinimumOsVersionDialog(
            onConfirm = {
                viewModel.updateMainState {
                    it.copy(
                        canDisplayMinimumOsDialog = false
                    )
                }
            },
            onDismiss = {
                viewModel.onEvent(
                    MainScreenEvent.HandleMinimumOsDialogDismiss
                )
            },
            translations = uiState.viewTranslations.minimumOSTranslations
        )
    }

}


