package com.prometheus_service.midas.core.domain.features.multi_language.use_case

import com.prometheus_service.midas.core.domain.features.multi_language.MultiLanguageRepository
import com.prometheus_service.midas.core.domain.features.multi_language.model.LocalizedModels
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMultiLanguageData @Inject constructor(
    private val repository: MultiLanguageRepository
) {
     operator fun invoke(locale: String): Flow<LocalizedModels?> {
        return repository.getLocalizedLanguageModel(locale = locale)
    }
}