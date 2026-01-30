package com.prometheus_service.midas.core.domain.shared.app_config.use_case

import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppConfigModel @Inject constructor(
    private val repository: AppConfigRepository
) {

    operator fun invoke(): Flow<AppConfigModel> {
        return repository.appConfigModel
    }
}