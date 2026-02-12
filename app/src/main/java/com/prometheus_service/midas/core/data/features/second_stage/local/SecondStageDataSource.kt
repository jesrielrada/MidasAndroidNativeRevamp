package com.prometheus_service.midas.core.data.features.second_stage.local

import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import kotlinx.coroutines.flow.Flow

interface SecondStageDataSource {
    fun getSecondStageModel(): Flow<SecondStageModel>
    suspend fun cacheSecondStageModel(data: SecondStageModel)
}