package com.prometheus_service.midas.core.data.features.remote_config.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import javax.annotation.processing.Generated

@Keep
@Generated
data class RemoteConfigDto(
    @SerializedName("data") val data : Data
)
@Keep
@Generated
data class Data(
    @SerializedName("android_version") val androidVersion: String,
    @SerializedName("android_native_version") val androidNativeVersion: String,
    @SerializedName("android_native_version_code") val androidNativeVersionCode: String,
    @SerializedName("android_native_update_file_size") val androidNativeUpdateFileSize: String?,
    @SerializedName("android_native_update_header") val androidNativeUpdateHeader: String?,
    @SerializedName("android_native_update_changes") val androidNativeUpdateChanges: List<String>?,
    @SerializedName("android_native_update_banners") val androidNativeUpdateBanners: List<String>?,
    @SerializedName("bti_url") val btiUrl: String?,
    @SerializedName("forgot_pass_url") val forgotPassUrl: String?,
    @SerializedName("clearConfigCache") val clearConfigCache: String?,
    @SerializedName("domains") val domains: List<String>?,
    @SerializedName("domains-pwa") val domainsPwa: List<String>?,
    @SerializedName("dl_domains") val dlDomains: DlDomains,
    @SerializedName("supported_languages") val supportedLanguages: List<String>?,
)
@Keep
@Generated
data class DlDomains(
    @SerializedName("default") val default: String,
    @SerializedName("active") val active: String,
    @SerializedName("fallback") val fallback: String?
)
