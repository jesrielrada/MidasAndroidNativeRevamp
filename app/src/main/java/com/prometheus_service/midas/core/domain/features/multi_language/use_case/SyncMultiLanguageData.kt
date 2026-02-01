package com.prometheus_service.midas.core.domain.features.multi_language.use_case

import com.prometheus_service.midas.core.domain.features.multi_language.MultiLanguageRepository
import javax.inject.Inject

class SyncMultiLanguageData @Inject constructor(
    private val repository: MultiLanguageRepository
) {
    suspend operator fun invoke(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ) {
        repository.syncMultiLanguageData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
            currency = currency
        )
    }
}