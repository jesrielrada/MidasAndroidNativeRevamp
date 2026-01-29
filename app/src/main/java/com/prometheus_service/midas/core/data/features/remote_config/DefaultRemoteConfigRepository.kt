package com.prometheus_service.midas.core.data.features.remote_config

import com.prometheus_service.midas.core.data.features.remote_config.local.RemoteConfigLocalDataSource
import com.prometheus_service.midas.core.data.features.remote_config.remote.RemoteConfigRemoteDataSource
import com.prometheus_service.midas.core.data.features.remote_config.util.mapper.toDomain
import com.prometheus_service.midas.core.domain.features.remote_config.RenameConfigRepository
import com.prometheus_service.midas.core.domain.features.remote_config.model.RemoteConfigModel
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultRemoteConfigRepository @Inject constructor(
    private val remoteDataSource: RemoteConfigRemoteDataSource,
    private val localDataSource: RemoteConfigLocalDataSource,
    private val dispatcherProvider: DispatcherProvider
) : RenameConfigRepository {

    override val remoteConfigModel: Flow<RemoteConfigModel>
        get() = localDataSource.getRemoteConfigModel()

    override suspend fun refreshRemoteConfigData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                val response = remoteDataSource.fetchRemoteConfig(
                    operatorId = operatorId,
                    userAgent = userAgent,
                    acceptLanguage = acceptLanguage,
                )
                val model = response.toDomain()
                localDataSource.cacheRemoteConfigModel(model)
                Timber.d("Successfully fetched and cached remote config data, model=$model")
            }.onFailure { exception ->
                Timber.e(exception, "Failed to fetch remote config data")
            }
        }
    }
}