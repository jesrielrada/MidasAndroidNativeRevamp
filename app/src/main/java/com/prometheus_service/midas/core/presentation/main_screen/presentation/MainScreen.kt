package com.prometheus_service.midas.core.presentation.main_screen.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prometheus_service.midas.core.presentation.features.language_selection.presentation.LanguageSelectionScreen
import com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.SplashScreen
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.TutorialScreen
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.WebviewScreen
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val shouldDisplayWebview = uiState.shouldDisplayWebview
    val webviewVisibility = if (shouldDisplayWebview) 1f else 0f

    WebviewScreen(
        modifier = Modifier.alpha(webviewVisibility),
        onPageFinished = {
            viewModel.onEvent(MainScreenEvent.UpdateWebviewReady)
        }
    )

    if (uiState.shouldDisplaySplash) {
        SplashScreen(
            onClickSkipBtn = {
                if (uiState.isWebviewReady) {
                    viewModel.onEvent(MainScreenEvent.HideSplashScreen)
                    viewModel.onEvent(MainScreenEvent.DisplayWebviewScreen)
                }
            },
            onScrollFinished = {
                if(uiState.isWebviewReady){
                    viewModel.onEvent(MainScreenEvent.HideSplashScreen)
                    viewModel.onEvent(MainScreenEvent.DisplayWebviewScreen)
                }
            },
            isReadyToHide = uiState.isWebviewReady
        )
    }

    if (uiState.shouldDisplayTutorial) {
        TutorialScreen()
    }

    if (uiState.shouldDisplayLanguageSelection) {
        LanguageSelectionScreen()
    }
}