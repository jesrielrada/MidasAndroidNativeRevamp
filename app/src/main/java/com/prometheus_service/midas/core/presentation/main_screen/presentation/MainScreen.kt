package com.prometheus_service.midas.core.presentation.main_screen.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.SplashScreen
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.TutorialScreen
import com.prometheus_service.midas.core.presentation.features.webview_screen.presentation.WebviewScreen

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val shouldDisplaySplash = uiState.value.shouldDisplaySplash
    val shouldDisplayTutorial = uiState.value.shouldDisplayTutorial

    val shouldDisplayWebview = uiState.value.shouldDisplayWebview
    val webviewVisibility = if (shouldDisplayWebview) 1f else 0f

    WebviewScreen(
        modifier = Modifier.alpha(webviewVisibility)
    )

    if (shouldDisplaySplash) {
        SplashScreen()
    }

    if (shouldDisplayTutorial) {
        TutorialScreen()
    }
}