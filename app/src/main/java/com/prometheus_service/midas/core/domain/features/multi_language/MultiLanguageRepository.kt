package com.prometheus_service.midas.core.domain.features.multi_language

import com.prometheus_service.midas.core.domain.features.multi_language.model.LocalizedModels
import kotlinx.coroutines.flow.Flow

interface MultiLanguageRepository {
    fun getLocalizedLanguageModel(locale: String): Flow<LocalizedModels>
    suspend fun syncMultiLanguageData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): Result<Unit>
}