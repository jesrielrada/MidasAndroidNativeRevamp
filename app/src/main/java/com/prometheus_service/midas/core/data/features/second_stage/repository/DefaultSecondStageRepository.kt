package com.prometheus_service.midas.core.data.features.second_stage.repository

import com.prometheus_service.midas.core.data.features.second_stage.local.SecondStageDataSource
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import com.prometheus_service.midas.core.domain.features.second_stage.repository.SecondStageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultSecondStageRepository @Inject constructor(
    private val localDataSource: SecondStageDataSource,
    private val dispatcherProvider: DefaultDispatcherProvider
) : SecondStageRepository {

    override val secondStageModel: Flow<SecondStageModel>
        get() = localDataSource.getSecondStageModel()

    override suspend fun cacheSecondStageModel(model: SecondStageModel): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                localDataSource.cacheSecondStageModel(model)
            }.onFailure { exception ->
                Timber.e(exception, "Failed to cache second stage model")
            }
        }
    }

}