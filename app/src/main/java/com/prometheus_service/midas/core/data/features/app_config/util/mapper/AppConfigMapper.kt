package com.prometheus_service.midas.core.data.features.app_config.util.mapper

import com.prometheus_service.midas.core.data.features.app_config.remote.model.AppConfigDto
import com.prometheus_service.midas.core.domain.features.app_config.model.AppConfigModel


fun AppConfigDto.toDomain(): AppConfigModel {
    val data = this.data
    return AppConfigModel(
        androidNativeVersion = data.androidNativeVersion,
        androidNativeVersionCode = data.androidNativeVersionCode,
        androidNativeUpdateFileSize = data.androidNativeUpdateFileSize,
        androidNativeUpdateHeader = data.androidNativeUpdateHeader,
        androidNativeUpdateChanges = data.androidNativeUpdateChanges,
        androidNativeUpdateBanners = data.androidNativeUpdateBanners,
        domainPwa = data.domainsPwa,
        downloadDomains = listOf(
            data.dlDomains.active,
            data.dlDomains.default,
            data.dlDomains.fallback,
        ),
        supportedLanguages = data.supportedLanguages
    )
}