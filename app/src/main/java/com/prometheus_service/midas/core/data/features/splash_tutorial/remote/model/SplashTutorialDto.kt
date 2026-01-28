package com.prometheus_service.midas.core.data.features.splash_tutorial.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class SplashTutorialDto(
    @SerializedName("data") val data: Data
)

@Keep
data class Data(
    @SerializedName("name") val name: String,
    @SerializedName("key") val key: String,
    @SerializedName("data") val data: _Data
)

@Keep
data class _Data(
    @SerializedName("android") val android: Android,
    @SerializedName("istutorialscreenliteenabled") val isTutorialScreenLitEnabled: String,
    @SerializedName("istutorialscreenenabled") val isTutorialScreenEnabled: String
)

@Keep
data class Android(
    @SerializedName("android_splash_images") val androidSplashImages: List<String>,
    @SerializedName("android_splash_lite_images") val androidSplashLiteImages: List<String>,
    @SerializedName("android_tutorial_images") val androidTutorialImages: List<String>,
    @SerializedName("android_tutorial_lite_images") val androidTutorialLiteImages: List<String>
)