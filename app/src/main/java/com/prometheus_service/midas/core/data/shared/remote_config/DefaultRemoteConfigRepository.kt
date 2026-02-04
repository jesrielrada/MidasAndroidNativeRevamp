package com.prometheus_service.midas.core.data.shared.remote_config

import com.prometheus_service.midas.core.data.shared.remote_config.local.RemoteConfigLocalDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.remote.RemoteConfigRemoteDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.util.mapper.toDomain
import com.prometheus_service.midas.core.domain.shared.remote_config.RemoteConfigRepository
import com.prometheus_service.midas.core.domain.shared.remote_config.model.RemoteConfigModel
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultRemoteConfigRepository @Inject constructor(
    private val remoteDataSource: RemoteConfigRemoteDataSource,
    private val localDataSource: RemoteConfigLocalDataSource,
    private val dispatcherProvider: DispatcherProvider
) : RemoteConfigRepository {

    override val remoteConfigModel: Flow<RemoteConfigModel>
        get() = localDataSource.getRemoteConfigModel()

    override suspend fun syncRemoteConfigData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String
    ): Result<RemoteConfigModel> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                val response = remoteDataSource.fetchRemoteConfig(
                    operatorId = operatorId,
                    userAgent = userAgent,
                    acceptLanguage = acceptLanguage,
                )
                val model = response.toDomain()
                localDataSource.cacheRemoteConfigModel(model)
                model
            }.onFailure { exception ->
                Timber.e(exception, "Failed to fetch remote config data")
            }
        }
    }
}