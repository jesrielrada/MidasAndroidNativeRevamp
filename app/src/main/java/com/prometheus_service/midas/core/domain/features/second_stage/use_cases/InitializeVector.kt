package com.prometheus_service.midas.core.domain.features.second_stage.use_cases

import javax.inject.Inject

class InitializeVector @Inject constructor() {
    operator fun invoke(clientSecret: String): String {
        return clientSecret.substring(0, 16)
    }
}