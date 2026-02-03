package com.prometheus_service.midas.core.data.shared.remote_config.util.mapper

import com.prometheus_service.midas.core.data.shared.remote_config.remote.model.RemoteConfigDto
import com.prometheus_service.midas.core.domain.shared.remote_config.model.RemoteConfigModel


fun RemoteConfigDto.toDomain(): RemoteConfigModel {
    val data = this.data
    return RemoteConfigModel(
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