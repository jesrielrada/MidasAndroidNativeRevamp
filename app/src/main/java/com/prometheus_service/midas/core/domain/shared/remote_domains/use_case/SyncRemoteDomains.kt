package com.prometheus_service.midas.core.domain.shared.remote_domains.use_case

import com.prometheus_service.midas.core.domain.shared.remote_domains.RemoteDomainsRepository
import javax.inject.Inject

class SyncRemoteDomains @Inject constructor(
    private val repository: RemoteDomainsRepository
) {
    suspend operator fun invoke(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ) {
        repository.syncRemoteDomainsData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
            currency = currency
        )
    }
}