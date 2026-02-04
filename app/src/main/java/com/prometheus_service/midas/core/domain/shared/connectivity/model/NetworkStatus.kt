package com.prometheus_service.midas.core.domain.shared.connectivity.model

data class NetworkStatus(
    val isConnected: Boolean = false,
    val connectionType: String = ""
)
