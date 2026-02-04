package com.prometheus_service.midas.core.domain.shared.connectivity

import com.prometheus_service.midas.core.domain.shared.connectivity.model.NetworkStatus
import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {
    fun observeNetworkStatus(): Flow<NetworkStatus>
}