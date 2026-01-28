package com.prometheus_service.midas.core.domain.features.remote_domains.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteDomainsModel(
    val updateStoredDomainsEnabled: Boolean = false,
    val reportFailingDomainsEnabled: Boolean = false,
    val reserveConfigDomains: List<String> = emptyList(),
    val reserveAppDomains: List<String> = emptyList(),
    val activeConfigDomains: List<String> = emptyList()
)

