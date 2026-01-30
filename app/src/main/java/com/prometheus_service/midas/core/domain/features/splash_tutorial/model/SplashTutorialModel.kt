package com.prometheus_service.midas.core.domain.features.splash_tutorial.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class SplashTutorialModel(
    val splashImages: List<String> = emptyList(),
    val tutorialImages: List<String> = emptyList(),
    val isTutorialScreenEnabled: Boolean = false
)


