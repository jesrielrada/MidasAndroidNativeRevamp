package com.prometheus_service.midas.core.presentation.features.language_selection.presentation

data class LanguageSelectionUiState(
    val supportedLocales: List<String> = listOf("Vietnamese", "English"),
    val header: String = "Select Language"
)
