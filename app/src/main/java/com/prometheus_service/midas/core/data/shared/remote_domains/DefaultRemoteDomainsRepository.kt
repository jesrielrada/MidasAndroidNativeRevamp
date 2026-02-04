package com.prometheus_service.midas.core.data.shared.remote_domains

import com.prometheus_service.midas.core.data.shared.remote_domains.local.RemoteDomainsLocalDataSource
import com.prometheus_service.midas.core.data.shared.remote_domains.remote.RemoteDomainsRemoteDataSource
import com.prometheus_service.midas.core.data.shared.remote_domains.util.mapper.toDomain
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.shared.remote_domains.RemoteDomainsRepository
import com.prometheus_service.midas.core.domain.shared.remote_domains.model.RemoteDomainsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class  DefaultRemoteDomainsRepository @Inject constructor(
    private val localDataSource: RemoteDomainsLocalDataSource,
    private val remoteDataSource: RemoteDomainsRemoteDataSource,
    private val dispatcherProvider: DefaultDispatcherProvider
) : RemoteDomainsRepository {

    override val remoteDomainsModel: Flow<RemoteDomainsModel>
        get() = localDataSource.getRemoteDomainsModel()

    override suspend fun syncRemoteDomainsData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                val response = remoteDataSource.fetchRemoteDomainsData(
                    operatorId = operatorId,
                    userAgent = userAgent,
                    acceptLanguage = acceptLanguage,
                    currency = currency
                )
                val model = response.toDomain()
                localDataSource.cacheRemoteDomainsModel(model)
            }.onFailure { exception ->
                Timber.d(exception, "Failed to fetch remote domains data")
            }
        }
    }
}