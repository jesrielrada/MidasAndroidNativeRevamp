package com.prometheus_service.midas.core.domain.shared.core.use_case

import com.prometheus_service.midas.cmspwaupdater.VersionInfo
import javax.inject.Inject


data class UpdateModel(
    val fileSize: String?,
    val header: String?,
    val changes: List<String>?,
    val banners: List<String>?,
    val version: String?,
    val versionCode: String?,
    val downloadUrl: String?
)

class InitializeUpdateVersionInfo @Inject constructor() {

    operator fun invoke(
        path: String,
        appIcon: Int,
        filename: String,
        model: UpdateModel
    ): VersionInfo {
        val domain = "${model.downloadUrl}$path"
        val file = "$filename${model.version}.apk"
        val versionCode = model.versionCode ?: "1"

        return VersionInfo(
            required = true,
            appIconResId = appIcon,
            version = Integer.valueOf(versionCode),
            downloadDomain = domain,
            file = file,
            fileSize = model.fileSize,
            updateHeader = model.header,
            changes = model.changes,
            bannerImages = model.banners
        )
    }
}