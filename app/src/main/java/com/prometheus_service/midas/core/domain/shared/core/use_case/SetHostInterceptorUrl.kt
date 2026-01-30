package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.core.domain.shared.interceptors.HostInterceptor
import javax.inject.Inject

class SetHostInterceptorUrl @Inject constructor(
    private val hostInterceptor: HostInterceptor
) {
    operator fun invoke(url: String) {
        hostInterceptor.setUrl(url)
    }
}