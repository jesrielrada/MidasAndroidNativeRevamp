package com.prometheus_service.midas.core.domain.shared.app_config.use_case

import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import javax.inject.Inject

class CacheAppConfigModel @Inject constructor(
    private val repository: AppConfigRepository
) {
    suspend operator fun invoke(model: AppConfigModel) {
        repository.cacheAppConfigModel(model)
    }
}