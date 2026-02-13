package com.prometheus_service.midas.core.domain.features.second_stage.use_cases

import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import com.prometheus_service.midas.core.domain.features.second_stage.repository.SecondStageRepository
import javax.inject.Inject

class CacheSecondStageConfig @Inject constructor(
    private val repository: SecondStageRepository
) {

    suspend operator fun invoke(model: SecondStageModel) {
        repository.cacheSecondStageModel(model)
    }
}