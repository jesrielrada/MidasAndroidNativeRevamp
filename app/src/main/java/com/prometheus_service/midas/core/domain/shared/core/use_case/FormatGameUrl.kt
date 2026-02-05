package com.prometheus_service.midas.core.domain.shared.core.use_case

import javax.inject.Inject

class FormatGameUrl @Inject constructor() {

    operator fun invoke(gamePath: String, baseUrl: String): String {
        if (gamePath.startsWith("http")) {
            return gamePath
        }

        if (!gamePath.startsWith("/")) {
            return "$baseUrl/$gamePath"
        }

        return baseUrl + gamePath
    }
}