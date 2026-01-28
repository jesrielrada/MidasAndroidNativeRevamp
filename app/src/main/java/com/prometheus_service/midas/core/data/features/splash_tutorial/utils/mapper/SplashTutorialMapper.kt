package com.prometheus_service.midas.core.data.features.splash_tutorial.utils.mapper

import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.model.SplashTutorialDto
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel


fun SplashTutorialDto.toDomain(): SplashTutorialModel {
    val data = this.data.data.android
    return SplashTutorialModel(
        splashImages = data.androidSplashImages,
        tutorialImages = data.androidTutorialImages,
    )
}