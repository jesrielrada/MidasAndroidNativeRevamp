package com.prometheus_service.midas.core.presentation.features.language_selection.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.core.domain.features.language_selection.model.Language
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.presentation.features.language_selection.event.LanguageSelectionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LanguageSelectionViewModel @Inject constructor(
    private val cacheAppConfigModel: CacheAppConfigModel,
    private val getAppConfigModel: GetAppConfigModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(LanguageSelectionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(LanguageSelectionEvent.InitializeLanguageSelectionScreen)
    }

    fun onEvent(event: LanguageSelectionEvent) {
        when (event) {
            is LanguageSelectionEvent.OnLanguageSelected -> {
                viewModelScope.launch {
                    Timber.d("Caching locale via CacheAppConfigModel use case")
                    val language = Language.fromDisplayName(event.language)
                    if (language != Language.UNKNOWN) {
                        cacheAppConfigModel(
                            AppConfigModel(
                                locale = language.locale,
                                isLanguageSelectionDisplayed = true
                            )
                        )
                    }
                }
            }

            LanguageSelectionEvent.InitializeLanguageSelectionScreen -> {
                viewModelScope.launch {
                    val isLanguageSelectionDisplayed =
                        getAppConfigModel.invoke().first().isLanguageSelectionDisplayed

                    Timber.d("Initializing language selection screen.. " +
                            "isLanguageSelectionDisplayed: $isLanguageSelectionDisplayed")

                    if (isLanguageSelectionDisplayed == true) {
                        _uiState.value = _uiState.value.copy(
                            canDisplayScreen = false
                        )
                    }
                }
            }
        }
    }
}