package com.prometheus_service.midas.core.domain.features.remote_config.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteConfigModel(
    val androidNativeVersion: String = "",
    val androidNativeVersionCode: String = "",
    val androidNativeUpdateFileSize: String? = null,
    val androidNativeUpdateHeader: String? = null,
    val androidNativeUpdateChanges: List<String>? = null,
    val androidNativeUpdateBanners: List<String>? = null,
    val domainPwa: List<String>? = null,
    val downloadDomains: List<String?>? = null,
    val supportedLanguages: List<String>? = null
)