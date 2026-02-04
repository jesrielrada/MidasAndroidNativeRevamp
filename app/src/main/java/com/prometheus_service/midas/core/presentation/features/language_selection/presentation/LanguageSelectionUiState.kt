package com.prometheus_service.midas.core.presentation.features.language_selection.presentation

import com.prometheus_service.midas.FlavorConfig

data class LanguageSelectionUiState(
    val supportedLocales: List<String> = listOf("Vietnamese", "English"),
    val header: String = FlavorConfig.LANGUAGE_SELECTION_HEADER
)
