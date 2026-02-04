package com.prometheus_service.midas.core.presentation.features.language_selection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.core.domain.features.language_selection.model.Language
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.presentation.features.language_selection.event.LanguageSelectionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor(
    private val cacheAppConfigModel: CacheAppConfigModel,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LanguageSelectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(LanguageSelectionEvent.InitializeLanguageSelectionList)
    }

    fun onEvent(event: LanguageSelectionEvent) {
        when (event) {
            is LanguageSelectionEvent.OnLanguageSelected -> {
                viewModelScope.launch {
                    if (event.locale != Language.UNKNOWN.locale) {
                        cacheAppConfigModel(
                            AppConfigModel(
                                locale = event.locale,
                                isLanguageSelectionDisplayed = true
                            )
                        )
                    }
                }
            }

            LanguageSelectionEvent.InitializeLanguageSelectionList -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            supportedLocales = listOf(
                                Language.ENGLISH.displayName,
                                Language.VIETNAMESE.displayName
                            )
                        )
                    }
                }
            }
        }
    }
}