package com.prometheus_service.midas.core.data.features.biometrics.data_source

import com.prometheus_service.midas.core.domain.features.biometrics.model.BiometricsModel
import kotlinx.coroutines.flow.Flow

interface BiometricsLocalDataSource {

    val biometricsModel: Flow<BiometricsModel>

    suspend fun cacheBiometricsModel(model: BiometricsModel)
}

