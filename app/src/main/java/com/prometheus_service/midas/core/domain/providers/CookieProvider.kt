package com.prometheus_service.midas.core.domain.providers

interface CookieProvider {
    suspend fun initializeNativeCookies(domain: String, version: String, language: String)

    suspend fun isLoggedIn(baseUrl: String): Boolean

    suspend fun persistCookies()

    suspend fun deleteSessionCookies(domain: String)
    suspend fun getCurrentCookies(domain: String) : String?

    suspend fun removeAllCookies() : Boolean

    suspend fun initializeSessionCookies(domain: String, cookies: String?)
}