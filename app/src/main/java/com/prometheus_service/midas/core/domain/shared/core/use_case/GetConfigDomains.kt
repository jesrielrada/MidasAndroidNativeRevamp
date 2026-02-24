package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.shared.remote_domains.use_case.GetRemoteDomains
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class GetConfigDomains @Inject constructor(
    private val remoteDomains: GetRemoteDomains,
    private val getAppConfigModel: GetAppConfigModel
) {
    suspend operator fun invoke(
        environment: String,
        uatDomains: List<String>,
        preprodDomains: List<String>,
        prodDomains: List<String>
    ): List<String> {
        val remoteConfig = remoteDomains().activeConfigDomains
        // 1. Determine base domains and log concisely using `.also`
        val baseDomains = if (remoteConfig.isNotEmpty()) {
            remoteConfig.also { Timber.d("Using remote domains") }
        } else when (environment) {
            "U" -> uatDomains.also { Timber.d("Using UAT domains") }
            "PP" -> preprodDomains.also { Timber.d("Using PREPROD domains") }
            else -> prodDomains.also { Timber.d("Using PROD domains") }
        }
        // 2. Return early if null
        val bestDomain = getAppConfigModel().first().bestDomain ?: return baseDomains
        // 3. Immutably place bestDomain at the front
        return listOf(bestDomain) + (baseDomains - bestDomain)
    }
}







