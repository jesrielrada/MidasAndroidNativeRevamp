package com.prometheus_service.midas.core.domain.shared.core.use_case

import javax.inject.Inject

class GetDomainFromUrl @Inject constructor() {
    operator fun invoke(url: String) : String {
        return url.removePrefix("https://")
            .substringAfter("pwa.")
            .substringAfter("m.")
            .substringAfter("epm.")
            .let { ".$it" }
    }
}