package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import javax.inject.Inject

class CacheAppCurrency @Inject constructor(
    private val appConfigRepository: AppConfigRepository
) {
    suspend operator fun invoke(data: String) {
        appConfigRepository.cacheAppCurrency(data)
    }
}