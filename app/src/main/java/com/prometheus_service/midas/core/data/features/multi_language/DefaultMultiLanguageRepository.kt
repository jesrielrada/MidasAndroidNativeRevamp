package com.prometheus_service.midas.core.data.features.multi_language

import com.prometheus_service.midas.core.data.features.multi_language.local.MultiLanguageLocalDataSource
import com.prometheus_service.midas.core.data.features.multi_language.remote.MultiLanguageRemoteDataSource
import com.prometheus_service.midas.core.data.features.multi_language.util.mapper.toDomain
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.features.multi_language.MultiLanguageRepository
import com.prometheus_service.midas.core.domain.features.multi_language.model.LocalizedModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultMultiLanguageRepository @Inject constructor(
    private val localDataSource: MultiLanguageLocalDataSource,
    private val remoteDataSource: MultiLanguageRemoteDataSource,
    private val dispatcherProvider: DefaultDispatcherProvider
) : MultiLanguageRepository {

    override fun getLocalizedLanguageModel(locale: String): Flow<LocalizedModels?> {
        return localDataSource.getLocalizedLanguageModel(locale)
    }

    override suspend fun refreshMultiLanguageData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                val response = remoteDataSource.fetchMultiLanguageSupport(
                    operatorId = operatorId,
                    userAgent = userAgent,
                    acceptLanguage = acceptLanguage,
                    currency = currency
                )
                val model = response.toDomain(acceptLanguage)
                localDataSource.cacheMultiLanguageModel(model)
                Timber.d("MultiLanguage data successfully synced and cached.")
            }.onFailure { exception ->
                Timber.e(exception, "Failed to refresh multi-language data from remote")
            }
        }
    }

}