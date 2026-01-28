package com.prometheus_service.midas.core.domain.features.remote_domains.use_case

import com.prometheus_service.midas.core.domain.features.remote_domains.RemoteDomainsRepository
import timber.log.Timber
import javax.inject.Inject

class GetRemoteDomains @Inject constructor(
    private val repository: RemoteDomainsRepository
) {
    suspend operator fun invoke(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ) {
        val result = repository.refreshRemoteDomainsData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
            currency = currency
        )
        Timber.d("Remote domains: $result")
    }
}