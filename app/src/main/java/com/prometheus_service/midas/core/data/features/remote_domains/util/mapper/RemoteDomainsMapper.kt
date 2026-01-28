package com.prometheus_service.midas.core.data.features.remote_domains.util.mapper

import com.prometheus_service.midas.core.data.features.remote_domains.remote.model.RemoteDomainsDto
import com.prometheus_service.midas.core.domain.features.remote_domains.model.RemoteDomainsModel

fun RemoteDomainsDto.toDomain(): RemoteDomainsModel {
    val data = this.data.data
    return RemoteDomainsModel(
        updateStoredDomainsEnabled = data.updateStoredDomainsEnabled.toBoolean(),
        reportFailingDomainsEnabled = data.reportFailingDomainsEnabled.toBoolean(),
        reserveConfigDomains = data.reserveConfigDomains,
        reserveAppDomains = data.reserveAppDomains,
        activeConfigDomains = data.activeConfigDomains
    )
}