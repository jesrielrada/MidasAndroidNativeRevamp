package com.prometheus_service.midas.core.data.shared.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.prometheus_service.midas.core.domain.shared.connectivity.ConnectivityRepository
import com.prometheus_service.midas.core.domain.shared.connectivity.model.NetworkStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class DefaultConnectivityRepository @Inject constructor(
    context: Context
) : ConnectivityRepository {

    private val connectivityManager = context.getSystemService(
        ConnectivityManager::class.java
    )

    private val connectionFlow = callbackFlow {
        trySend(getCurrentNetworkStatus())

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(NetworkStatus())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                val hasInternet =
                    networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_INTERNET
                    ) && networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )

                val isConnectedToMobile =
                    networkCapabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_CELLULAR
                    )

                val isConnectedToWifi =
                    networkCapabilities.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    )

                val isConnectedToVpn =
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)


                // Check if connected only to VPN without an underlying network
                if (isConnectedToVpn && !isConnectedToWifi && !isConnectedToMobile) {
                    // This state represents a VPN connection without a direct Wi-Fi or Mobile connection.
                    // It often implies no internet access, so we'll treat it as not connected.
                    trySend(NetworkStatus(isConnected = false))
                    return
                }

                if (hasInternet) {
                    val type = getNetworkType(isConnectedToMobile, isConnectedToWifi)
                    trySend(
                        NetworkStatus(
                            isConnected = true,
                            connectionType = type
                        )
                    )
                }
            }
        }
        subscribe(callback)
        awaitClose {
            unsubscribe(callback)
        }
    }

    private fun getCurrentNetworkStatus(): NetworkStatus {
        val activeNetwork = connectivityManager.activeNetwork
        val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isWifi = caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        val isMobile = caps?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        val hasInternet = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true

        return if (hasInternet) {
            NetworkStatus(isConnected = true, connectionType = getNetworkType(isMobile, isWifi))
        } else {
            NetworkStatus(isConnected = false)
        }
    }

    private fun getNetworkType(isMobile: Boolean, isWifi: Boolean): String {
        if (isMobile) {
            return "MOBILE"
        } else if (isWifi) {
            return "WIFI"
        }
        return "OTHERS"
    }

    private fun subscribe(networkCallback: ConnectivityManager.NetworkCallback) {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun unsubscribe(networkCallback: ConnectivityManager.NetworkCallback) {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun observeNetworkStatus(): Flow<NetworkStatus> {
        return connectionFlow.distinctUntilChanged().conflate()
    }

}