package com.prometheus_service.midas.core.presentation.main_screen.presentation

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.biometric.BiometricPrompt.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.prometheus_service.midas.core.presentation.features.biometrics.BiometricAuthenticator
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.GameScreen
import com.prometheus_service.midas.core.presentation.features.language_selection.presentation.LanguageSelectionScreen
import com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.SplashScreen
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.TutorialScreen
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.WebviewScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.*
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenSideEffect
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.BiometricsTranslations
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
    val authenticator = remember(activity) { BiometricAuthenticator(activity) }

    val shouldDisplayWebview = uiState.shouldDisplayWebview
    val shouldDisplayGameView = uiState.shouldDisplayGameView
    val webviewVisibility = if (shouldDisplayWebview) 1f else 0f

    val shouldRestartSplash by remember { derivedStateOf { uiState.isErrorDialogVisible } }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is MainScreenSideEffect.DisplayBiometricAuthError -> {

                    val negative =
                        uiState.viewTranslations.biometricsTranslations.biometricErrorCancelled
                    val lockout =
                        uiState.viewTranslations.biometricsTranslations.biometricErrorLockout

                    val errorMessage = when (effect.code) {
                        ERROR_CANCELED,
                        ERROR_USER_CANCELED,
                        ERROR_NEGATIVE_BUTTON -> {
                            negative
                        }

                        ERROR_LOCKOUT,
                        ERROR_LOCKOUT_PERMANENT -> {
                            lockout
                        }

                        else -> effect.message
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

                is MainScreenSideEffect.DisplayBiometricSelectionList -> {
                    val cancelBtnLabel =
                        uiState.viewTranslations.biometricsTranslations.promptCancel
                    val selectAccountLabel =
                        uiState.viewTranslations.biometricsTranslations.dialogSelectAccount
                    MaterialAlertDialogBuilder(context).apply {
                        setTitle(selectAccountLabel)
                        setItems(effect.usernames?.toTypedArray()) { dialog, index ->
                            effect.usernames?.get(index)?.let {
                                viewModel.onEvent(HandleAccountSelected(it))
                            }
                        }
                        setNegativeButton(cancelBtnLabel) { _, _ ->
                            viewModel.onEvent(HandleAccountSelectionAuthCancelled)
                        }
                        show()
                    }
                }

                MainScreenSideEffect.DisplayBiometricSuccessEnrollment -> {
                    Toast.makeText(
                        context,
                        uiState.viewTranslations.biometricsTranslations.biometricToastMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MainScreenSideEffect.DisplayBiometricPrompt -> {
                    Timber.d("Displaying biometric prompt...")

                    val promptInfo = PromptInfo.Builder()
                        .setTitle(uiState.viewTranslations.biometricsTranslations.promptTitle)
                        .setNegativeButtonText(uiState.viewTranslations.biometricsTranslations.promptCancel)
                        .setConfirmationRequired(false)
                        .build()

                    authenticator.authenticate(
                        promptInfo = promptInfo,
                        cryptoObject = CryptoObject(effect.cipher),
                        onSuccess = { result ->
                            if (effect.isFromAccountSelection) {
                                Timber.d("Biometric authentication succeeded, is from account selection .. ")
                                viewModel.onEvent(HandleAccountSelectedAuthSucceed(result))
                            } else {
                                Timber.d("Biometric authentication succeeded, is not from account selection .. ")
                                viewModel.onEvent(HandleBiometricsAuthResult(result))
                            }
                        },
                        onError = { code, msg ->
                            viewModel.onEvent(HandleBiometricsAuthError(code = code, message = msg))
                        }
                    )

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
            },
            onShouldDisplayBiometricsLogin = {
                viewModel.onEvent(DisplayBiometricAccountSelection)
            },
            onLoginLauncher = {
                Timber.d("Login launcher called")
                viewModel.onEvent(HandleBiometricsLogin)
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
                    viewModel.onEvent(LoadBaseUrl)
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
    }
}

@Composable
fun BiometricsErrorDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    translations: BiometricsTranslations
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(translations.currentDialogTitle) },
        text = {
            Text(
                text = translations.currentDialogMessage,
                color = Color.White
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = translations.currentDialogButtonLabel,
                    color = Color.White
                )
            }
        }
    )
}


@Composable
fun BiometricsEnableDialog(
    onConfirm: () -> Unit,
    onDontShowAgain: () -> Unit,
    onDismiss: () -> Unit,
    translations: BiometricsTranslations
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(translations.dialogEnableTitle) },
        text = {
            Text(
                text = translations.dialogEnableMessage,
                color = Color.White
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = translations.dialogPositiveBtnLabel,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDontShowAgain) {
                Text(
                    text = translations.dialogNeutralBtnLabel,
                    color = Color.White
                )
            }
        }
    )
}

@Composable
fun BiometricsLoadingDialog(onDismissRequest: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}