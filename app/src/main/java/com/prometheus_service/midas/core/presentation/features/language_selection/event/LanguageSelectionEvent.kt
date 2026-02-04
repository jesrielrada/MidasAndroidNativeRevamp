package com.prometheus_service.midas.core.presentation.features.language_selection.event

sealed class LanguageSelectionEvent {
    object InitializeLanguageSelectionList : LanguageSelectionEvent()
    data class OnLanguageSelected(val locale: String) : LanguageSelectionEvent()
}