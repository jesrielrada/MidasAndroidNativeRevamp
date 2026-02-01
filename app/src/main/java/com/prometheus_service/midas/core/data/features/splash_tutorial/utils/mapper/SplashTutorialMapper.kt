package com.prometheus_service.midas.core.data.features.splash_tutorial.utils.mapper

import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.model.SplashTutorialDto
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import com.prometheus_service.midas.core.domain.shared.interceptors.HostInterceptor
import timber.log.Timber


fun SplashTutorialDto.toDomain(interceptor: HostInterceptor): SplashTutorialModel {
    val baseUrl = interceptor.getBaseUrl()?.trim()?.removeSuffix("/")
    val data = this.data.data
    Timber.d("Mapping splash dto, base url is: $baseUrl")
    return SplashTutorialModel(
        splashImages = data.android.androidSplashImages.map { path ->
            buildImageUrl(baseUrl, path)
        },
        tutorialImages = data.android.androidTutorialImages.map { path ->
            buildImageUrl(baseUrl, path)
        },
        isTutorialScreenEnabled = data.isTutorialScreenEnabled.toBoolean()
    )
}

/**
 * Helper to ensure the URL is well-formed.
 * Prevents double slashes and ensures the scheme exists.
 */
private fun buildImageUrl(baseUrl: String?, path: String): String {
    val cleanPath = path.removePrefix("/")
    val fullUrl = "$baseUrl/$cleanPath"
    // Safety check: Ensure the result actually looks like a URL
    return if (!fullUrl.startsWith("http")) {
        "https://$fullUrl"
    } else {
        fullUrl
    }
}