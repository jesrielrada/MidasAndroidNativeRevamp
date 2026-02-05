package com.prometheus_service.midas.core.domain.providers

interface CookieProvider {
    fun initializeNativeCookies(domain: String, version: String, language: String)
}