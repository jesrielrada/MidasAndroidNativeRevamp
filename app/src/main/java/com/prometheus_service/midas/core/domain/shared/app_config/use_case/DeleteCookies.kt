package com.prometheus_service.midas.core.domain.shared.app_config.use_case

import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import timber.log.Timber
import javax.inject.Inject

class DeleteCookies @Inject constructor(
    private val appConfigRepository: AppConfigRepository
) {
    suspend operator fun invoke() {
        Timber.d("Deleting cookies ...")
        appConfigRepository.deleteCookies()
    }
}