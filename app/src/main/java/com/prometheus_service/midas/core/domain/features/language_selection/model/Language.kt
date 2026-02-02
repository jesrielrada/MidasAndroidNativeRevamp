package com.prometheus_service.midas.core.domain.features.language_selection.model

enum class Language(val displayName: String, val locale: String) {
    VIETNAMESE("Tiếng Việt - Vietnamese", "vi"),
    ENGLISH("English(US) - English(US)", "en"),
    UNKNOWN("Unknown", "");

    companion object {
        fun fromDisplayName(name: String) : Language {
            return entries.find { it.displayName == name } ?: UNKNOWN
        }
    }
}