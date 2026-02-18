package com.prometheus_service.midas.core.presentation.features.pushy.utils

import android.content.Context
import java.util.Locale

fun getPushyLanguage(lang: String?): String {
    if (lang == "id") return "in"
    return lang ?: ""
}

fun getDeviceLanguage(context: Context): Locale {
    return context.resources.configuration.locales.get(0)
}

fun isLanguageEqual(deviceLang: String, countryLang: String): Boolean {
    return (deviceLang.contains("en") && countryLang.contains("en") ||
            deviceLang.contains("km") && countryLang.contains("km") ||
            deviceLang.contains("zh") && countryLang.contains("zh") ||
            deviceLang.contains("id") && countryLang.contains("id") ||
            deviceLang.contains("ja") && countryLang.contains("ja") ||
            deviceLang.contains("ko") && countryLang.contains("ko") ||
            deviceLang.contains("th") && countryLang.contains("th") ||
            deviceLang.contains("vi") && countryLang.contains("vi") ||
            deviceLang.contains("in") && countryLang.contains("in")
            )
}