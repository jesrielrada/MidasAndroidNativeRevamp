package com.prometheus_service.midas.core.domain.features.second_stage.repository

import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import kotlinx.coroutines.flow.Flow

interface SecondStageRepository {

    val secondStageModel: Flow<SecondStageModel>

    suspend fun cacheSecondStageModel(model: SecondStageModel) : Result<Unit>
}