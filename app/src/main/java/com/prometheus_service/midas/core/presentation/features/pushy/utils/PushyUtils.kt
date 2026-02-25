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

fun isLanguageNotEmpty(deviceLang: String, countryLang: String): Boolean {
    return deviceLang.isNotEmpty() && countryLang.isNotEmpty()
}

fun processNotificationUrl(url: String): String? {
    return when {
        url.isEmpty() -> return null
        url.contains("funds") -> {
            val lastSegment = url.removeSuffix("/").substringAfterLast('/')
            val routeName =
                if (lastSegment.contains("deposit-withdrawal")) "history" else lastSegment
            "javascript: window.pwa.navigate({name:'$routeName-route'})"
        }

        url.contains("/promotions") -> {
            val lastSegment = url.removeSuffix("/").substringAfterLast('/')
            if (lastSegment.isEmpty() || lastSegment.contains("promotions")) {
                "javascript: window.pwa.navigate({name:'promotion-route'})"
            }
            val hasLetters = lastSegment.contains(Regex("[A-Za-z]"))
            val hasDigits = lastSegment.contains(Regex("\\d"))

            if (hasLetters && hasDigits) {
                val category = lastSegment.substringBefore('?')
                val id = lastSegment.substringAfter('=')
                "javascript: window.pwa.navigate({ name : 'promotion-route', params : { category : '$category', id : '$id'}})"
            } else {
                "javascript: window.pwa.navigate({ name : 'promotion-route', params : { category : '$lastSegment'}})"
            }
        }

        url.contains("slots") -> {
            val vendor = url.substringAfterLast('/')
            "javascript: window.pwa.navigate({name: 'slot-vendor-route', 'params' : { 'vendor' : '$vendor'}})"
        }

        url.contains("launcher") ||
                url.contains("referralId") ||
                url.contains("tracker") ||
                url.contains("register.aspx") ||
                url.contains("affiliateid") -> {
            url
        }

        else -> {
            val route = url.substringAfterLast('/')
            "javascript: window.pwa.navigate({ name: '$route-route'})"
        }
    }
}