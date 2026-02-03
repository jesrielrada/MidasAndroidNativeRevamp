package com.prometheus_service.midas.core.data.shared.multi_language.remote

import com.prometheus_service.midas.core.data.shared.multi_language.remote.model.MultiLanguageDto

interface MultiLanguageRemoteDataSource {
    suspend fun fetchMultiLanguageSupport(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): MultiLanguageDto
}