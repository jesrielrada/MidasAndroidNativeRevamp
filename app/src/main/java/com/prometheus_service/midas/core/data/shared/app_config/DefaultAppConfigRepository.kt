package com.prometheus_service.midas.core.data.shared.app_config

import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.data.shared.app_config.local.AppConfigLocalDataSource
import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultAppConfigRepository @Inject constructor(
    private val localDataSource: AppConfigLocalDataSource,
    private val dispatcherProvider: DefaultDispatcherProvider
) : AppConfigRepository {

    override val appConfigModel: Flow<AppConfigModel>
        get() = localDataSource.getAppConfigModel()

    override suspend fun cacheAppConfigModel(model: AppConfigModel): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                localDataSource.cacheAppConfigModel(model)
                Timber.d("Successfully cached app config model")
            }.onFailure { exception ->
                Timber.e(exception, "Failed to cache app config model")
            }
        }
    }
}
