package com.prometheus_service.midas.core.domain.shared.remote_domains.use_case

import com.prometheus_service.midas.core.domain.shared.remote_domains.RemoteDomainsRepository
import com.prometheus_service.midas.core.domain.shared.remote_domains.model.RemoteDomainsModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetRemoteDomains @Inject constructor(
    private val repository: RemoteDomainsRepository
) {
    suspend operator fun invoke(): RemoteDomainsModel {
        return repository.remoteDomainsModel.first()
    }
}