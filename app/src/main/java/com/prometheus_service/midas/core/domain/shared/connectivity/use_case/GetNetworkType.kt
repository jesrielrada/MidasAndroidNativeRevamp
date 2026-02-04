package com.prometheus_service.midas.core.domain.shared.connectivity.use_case

import com.prometheus_service.midas.core.domain.shared.connectivity.ConnectivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetNetworkType @Inject constructor(
    private val repository: ConnectivityRepository
) {
    operator fun invoke(): Flow<String> {
        return repository.observeNetworkStatus()
            .filter { it.isConnected && it.connectionType.isNotEmpty() }
            .map { it.connectionType }
            .distinctUntilChanged()
    }
}