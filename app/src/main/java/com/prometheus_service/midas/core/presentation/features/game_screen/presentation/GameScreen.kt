package com.prometheus_service.midas.core.presentation.features.game_screen.presentation

import android.annotation.SuppressLint
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.components.game_backdrop.GameBackdrop
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.components.game_progress.GameProgress
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.components.side_fab.GameSideFab
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.components.webview.GameWebview
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.event.GameScreenEvent
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.GameScreenTranslations
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameScreenViewModel = hiltViewModel(),
    gameUrl: String? = null,
    gameScreenTranslations: GameScreenTranslations,
    onReturnDialogConfirm: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onDepositButtonClicked: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val configuration = LocalConfiguration.current

    LaunchedEffect(Unit) {
        Timber.d("Checking login state ...")
        viewModel.onEvent(GameScreenEvent.CheckLoginState)
    }

    LaunchedEffect(configuration.orientation) {
        if (configuration.orientation == ORIENTATION_LANDSCAPE) {
            Timber.d("Orientation set to landscape..")
            viewModel.onEvent(GameScreenEvent.OnOrientationChanged)
        } else {
            Timber.d("Orientation set to portrait..")
            viewModel.onEvent(GameScreenEvent.OnOrientationChanged)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Timber.d("Game screen disposing ... ")
            viewModel.onEvent(GameScreenEvent.ResetUiState)
        }
    }

    GameScreenContent(
        modifier = modifier,
        uiState = uiState,
        gameUrl = gameUrl,
        gameScreenTranslations = gameScreenTranslations,
        onEvent = viewModel::onEvent,
        onReturnDialogConfirm = onReturnDialogConfirm,
        onHomeButtonClicked = onHomeButtonClicked,
        onClickDepositButton = {
            onDepositButtonClicked(it)
        }
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GameScreenContent(
    modifier: Modifier = Modifier,
    uiState: GameScreenUiState,
    gameUrl: String? = null,
    gameScreenTranslations: GameScreenTranslations,
    onEvent: (GameScreenEvent) -> Unit,
    onReturnDialogConfirm: () -> Unit,
    onHomeButtonClicked: () -> Unit,
    onClickDepositButton: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                onEvent(GameScreenEvent.OnGloballyPositioned(it))
            }
    ) {
        GameWebview(
            gameUrl = gameUrl,
            onGameLoaded = {
                onEvent(GameScreenEvent.HideProgressView)
            }
        )
        GameBackdrop(
            shouldDisplayBackdrop = uiState.displayBackdrop,
            onClickBackdrop = {
                onEvent(GameScreenEvent.SetSideFabClose)
            }
        )
        GameSideFab(
            uiState = uiState,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .onGloballyPositioned {
                    onEvent(GameScreenEvent.OnSideFabGloballyPositioned(it))
                }
                .offset {
                    IntOffset(0, uiState.sideFabOffsetY.value.roundToInt())
                },
            onClickSideFab = {
                if (uiState.sideFabState == GameSideFabState.OPEN) {
                    onEvent(GameScreenEvent.SetSideFabClose)
                } else {
                    onEvent(GameScreenEvent.SetSideFabOpen)
                }
            },
            onClickReturnButton = { onEvent(GameScreenEvent.OnClickReturnButton) },
            onClickHomeButton = onHomeButtonClicked,
            onClickDepositButton = {
                if (uiState.isAccountLoggedIn) {
                    val route = "javascript: window.pwa.navigate({ name: 'deposit-route'})"
                    onClickDepositButton(route)
                } else {
                    val route = "javascript: window.pwa.navigate({ name: 'login-route'})"
                    onClickDepositButton(route)
                }
            }

        )
        GameProgress(
            isLoading = uiState.isLoading
        )

        if (uiState.shouldDisplayReturnDialog) {
            GameReturnDialog(
                gameScreenTranslations = gameScreenTranslations,
                onReturnDialogConfirm = onReturnDialogConfirm,
                onReturnDialogDismiss = {
                    onEvent(GameScreenEvent.HideReturnDialog)
                }
            )
        }
    }
}


@Composable
fun GameReturnDialog(
    gameScreenTranslations: GameScreenTranslations,
    onReturnDialogDismiss: () -> Unit,
    onReturnDialogConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            //do nothing
        },
        dismissButton = {
            TextButton(onClick = {
                onReturnDialogDismiss()
            }) {
                Text(
                    color = Color.White,
                    text = gameScreenTranslations.returnDialogCancel
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onReturnDialogConfirm()
            }) {
                Text(
                    color = Color.White,
                    text = gameScreenTranslations.returnDialogConfirm
                )
            }
        },
        text = {
            Text(
                color = Color.White,
                text = gameScreenTranslations.returnDialogMessage
            )
        }
    )
}