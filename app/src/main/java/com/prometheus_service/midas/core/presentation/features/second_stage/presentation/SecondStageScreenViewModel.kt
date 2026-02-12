package com.prometheus_service.midas.core.presentation.features.second_stage.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SecondStageScreenViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SecondStageScreenUiState())
    val uiState: StateFlow<SecondStageScreenUiState> = _uiState.asStateFlow()
}