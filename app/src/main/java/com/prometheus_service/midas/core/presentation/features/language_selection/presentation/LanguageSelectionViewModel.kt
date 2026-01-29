package com.prometheus_service.midas.core.presentation.features.language_selection.presentation

import androidx.lifecycle.ViewModel
import com.prometheus_service.midas.core.presentation.features.language_selection.event.LanguageSelectionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(LanguageSelectionUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LanguageSelectionEvent) {
        when (event) {
            is LanguageSelectionEvent.OnLanguageSelected -> TODO()
        }
    }
}