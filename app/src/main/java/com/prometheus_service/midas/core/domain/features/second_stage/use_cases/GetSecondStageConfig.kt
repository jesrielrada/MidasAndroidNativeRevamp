package com.prometheus_service.midas.core.domain.features.second_stage.use_cases

import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import com.prometheus_service.midas.core.domain.features.second_stage.repository.SecondStageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSecondStageConfig @Inject constructor(
    private val repository: SecondStageRepository
) {

    fun invoke(): Flow<SecondStageModel> {
        return repository.secondStageModel
    }
}