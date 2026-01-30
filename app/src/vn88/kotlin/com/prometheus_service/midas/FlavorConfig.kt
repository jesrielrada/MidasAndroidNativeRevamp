package com.prometheus_service.midas

object FlavorConfig {
    const val ENVIRONMENT: String = BuildConfig.BuildEnv
    const val VERSION_STRING: String = BuildConfig.VERSION_NAME
    const val OPERATOR_ID = "vn88"
    const val DEFAULT_LOCALE = "vi"
    const val INITIAL_USER_AGENT = "VN88MobileA/$VERSION_STRING"
    const val LANGUAGE_SELECTION_HEADER = "Chọn ngôn ngữ"

    val DOMAINS_UAT = listOf("https://epm.vn88uat.com")
    val DOMAINS_PREPROD = listOf("https://vn88m.gpintdemo.com")
    val DOMAINS_PROD = listOf(
        "https://m.88vnsicbo.com",
        "https://m.88vnsicbo.vip",
        "https://m.88vnsicbo.cc"
    )
}