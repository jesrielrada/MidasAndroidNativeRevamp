package com.prometheus_service.midas.core.domain.shared.connectivity.use_case

import com.prometheus_service.midas.core.domain.shared.connectivity.ConnectivityRepository
import com.prometheus_service.midas.core.domain.shared.connectivity.model.NetworkStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveNetwork @Inject constructor(
    private val repository: ConnectivityRepository
) {
    operator fun invoke(): Flow<NetworkStatus> {
        return repository.observeNetworkStatus()
    }
}