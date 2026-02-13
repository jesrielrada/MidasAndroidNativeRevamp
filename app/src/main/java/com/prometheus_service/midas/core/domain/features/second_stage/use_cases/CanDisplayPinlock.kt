package com.prometheus_service.midas.core.domain.features.second_stage.use_cases

import com.prometheus_service.midas.core.domain.features.second_stage.repository.SecondStageRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class CanDisplayPinlock @Inject constructor(
    private val secondStageRepository: SecondStageRepository
) {

    suspend operator fun invoke(): Boolean {
        val model = secondStageRepository.secondStageModel.first()
        val hasCachedPin = model.pin != null
        val isUserEnabled = model.isUserEnabled != null && model.isUserEnabled
        val isCmsboEnabled = model.isCmsboEnabled != null && model.isCmsboEnabled

        Timber.d("CanDisplayPinlock... model: $model")
        return hasCachedPin && isUserEnabled && isCmsboEnabled
    }
}