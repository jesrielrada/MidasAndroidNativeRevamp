package com.prometheus_service.midas.core.domain.shared.app_config.model

import kotlinx.serialization.Serializable

/**
 * Should have initial value of null so there will be
 * no issues on caching on data store
 */

@Serializable
data class AppConfigModel(
    val version: String? = null,
    val locale: String? = null,
    val baseUrl: String? = null,
    val domain: String? = null,
    val bestDomain: String? = null,
    val currency: String? = null,
    val sessionCookies: String? = null,
    val isTutorialDisplayed: Boolean? = null,
    val isLanguageSelectionDisplayed: Boolean? = null
)