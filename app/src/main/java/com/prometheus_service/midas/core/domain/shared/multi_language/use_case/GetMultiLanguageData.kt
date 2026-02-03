package com.prometheus_service.midas.core.domain.shared.multi_language.use_case

import com.prometheus_service.midas.core.domain.shared.multi_language.MultiLanguageRepository
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedModels
import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetMultiLanguageData @Inject constructor(
    private val repository: MultiLanguageRepository,
    private val appConfigRepository: AppConfigRepository
) {
    suspend operator fun invoke(locale: String): Flow<LocalizedModels> {
        val locale = appConfigRepository.appConfigModel.first().locale ?: locale
        return repository.getLocalizedLanguageModel(locale = locale)
    }
}