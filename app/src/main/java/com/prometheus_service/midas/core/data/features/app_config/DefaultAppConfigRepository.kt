package com.prometheus_service.midas.core.data.features.app_config

import com.prometheus_service.midas.core.data.features.app_config.local.AppConfigLocalDataSource
import com.prometheus_service.midas.core.data.features.app_config.remote.AppConfigRemoteDataSource
import com.prometheus_service.midas.core.data.features.app_config.util.mapper.toDomain
import com.prometheus_service.midas.core.data.features.splash_tutorial.utils.mapper.toDomain
import com.prometheus_service.midas.core.domain.features.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.features.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultAppConfigRepository @Inject constructor(
    private val remoteDataSource: AppConfigRemoteDataSource,
    private val localDataSource: AppConfigLocalDataSource,
    private val dispatcherProvider: DispatcherProvider
) : AppConfigRepository {

    override val appConfigModel: Flow<AppConfigModel>
        get() = localDataSource.getAppConfigModel()

    override suspend fun refreshAppConfigData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                val response = remoteDataSource.fetchApplicationConfig(
                    operatorId = operatorId,
                    userAgent = userAgent,
                    acceptLanguage = acceptLanguage,
                )
                val model = response.toDomain()
                localDataSource.cacheAppConfigModel(model)
                Timber.d("Successfully fetched and cached app config data, model=$model")
            }.onFailure { exception ->
                Timber.d(exception, "Failed to fetch app config data")
            }
        }
    }
}