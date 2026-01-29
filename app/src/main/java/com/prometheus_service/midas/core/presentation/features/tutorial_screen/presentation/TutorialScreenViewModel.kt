package com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TutorialScreenViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(TutorialScreeUiState())
    val uiState = _uiState.asStateFlow()

}