package com.prometheus_service.midas.core.presentation.features.webview_screen.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(WebViewScreenUiState())
    val uiState = _uiState.asStateFlow()
}


