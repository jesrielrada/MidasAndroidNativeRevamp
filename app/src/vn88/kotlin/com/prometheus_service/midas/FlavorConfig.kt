package com.prometheus_service.midas

import androidx.compose.ui.graphics.Color

object FlavorConfig {
    const val VERSION_STRING: String = BuildConfig.VERSION_NAME
    const val OPERATOR_ID = "vn88"
    const val CLIENT_SECRET = "1FD469D5AED1F262A28C847FE60FCAEE"
    const val DEFAULT_LOCALE = "vi"
    const val INITIAL_USER_AGENT = "VN88MobileA/$VERSION_STRING"
    const val LANGUAGE_SELECTION_HEADER = "Chọn ngôn ngữ"

    val DEFAULT_COLOR = Color(0XFF901B11)

    val DOMAINS_UAT = listOf(
        "https://epm1.vn88uat.com",
        "https://epm2.vn88uat.com",
        "https://m.vn88uat.com"
    )
    val DOMAINS_PREPROD = listOf("https://vn88m.gpintdemo.com")
    val DOMAINS_PROD = listOf(
        "https://m.88vnsicbo.com",
        "https://m.88vnsicbo.vip",
        "https://m.88vnsicbo.cc"
    )
}