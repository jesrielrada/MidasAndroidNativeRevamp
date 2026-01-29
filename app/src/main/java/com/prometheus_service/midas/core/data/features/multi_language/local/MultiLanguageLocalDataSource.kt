package com.prometheus_service.midas.core.data.features.multi_language.local

import com.prometheus_service.midas.core.domain.features.multi_language.model.LocalizedModels
import com.prometheus_service.midas.core.domain.features.multi_language.model.MultiLanguageModel
import kotlinx.coroutines.flow.Flow

interface MultiLanguageLocalDataSource {
    fun getLocalizedLanguageModel(locale: String): Flow<LocalizedModels?>
    suspend fun cacheMultiLanguageModel(model: MultiLanguageModel)
}