package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.providers.CookieProvider
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAccountLoggedInState @Inject constructor(
    private val provider: CookieProvider,
    private val getAppConfigModel: GetAppConfigModel,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(): Boolean {
        return withContext(dispatcherProvider.io) {
            getAppConfigModel().firstOrNull()?.baseUrl?.let { baseUrl -> provider.isLoggedIn(baseUrl) } ?: false
        }
    }
}