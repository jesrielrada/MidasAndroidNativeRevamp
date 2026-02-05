package com.prometheus_service.midas.core.domain.providers

interface CookieProvider {
    suspend fun initializeNativeCookies(domain: String, version: String, language: String)

    suspend fun isLoggedIn(baseUrl: String): Boolean
}