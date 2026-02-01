package com.prometheus_service.midas.core.data.features.splash_tutorial

import com.prometheus_service.midas.core.data.features.splash_tutorial.local.SplashTutorialLocalDataSource
import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.SplashTutorialRemoteDataSource
import com.prometheus_service.midas.core.data.features.splash_tutorial.utils.mapper.toDomain
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.features.splash_tutorial.SplashTutorialRepository
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import com.prometheus_service.midas.core.domain.shared.interceptors.HostInterceptor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultSplashTutorialRepository @Inject constructor(
    private val remoteDataSource: SplashTutorialRemoteDataSource,
    private val localDataSource: SplashTutorialLocalDataSource,
    private val dispatcherProvider: DefaultDispatcherProvider,
    private val hostInterceptor: HostInterceptor
) : SplashTutorialRepository {

    override val splashTutorialModel: Flow<SplashTutorialModel>
        get() = localDataSource.getSplashTutorialModel()

    override suspend fun syncSplashTutorialData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                val response = remoteDataSource.fetchSplashTutorialData(
                    operatorId = operatorId,
                    userAgent = userAgent,
                    acceptLanguage = acceptLanguage,
                    currency = currency
                )
                val model = response.toDomain(hostInterceptor)
                localDataSource.cacheSplashTutorialModel(model)
                Timber.d("Successfully fetched and cached splash tutorial data, model=$model")
            }.onFailure { exception ->
                Timber.e(exception, "Failed to fetch splash tutorial data")
            }
        }
    }
}