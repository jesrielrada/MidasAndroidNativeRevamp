package com.prometheus_service.midas.core.presentation.features.language_selection.event

sealed class LanguageSelectionEvent {
    data class OnLanguageSelected(val language: String) : LanguageSelectionEvent()
}