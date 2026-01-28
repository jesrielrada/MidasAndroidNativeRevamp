package com.prometheus_service.midas.core.data.features.splash_tutorial.remote

import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.model.SplashTutorialDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultSplashTutorialRemoteDataSource @Inject constructor(
    private val apiService: SplashTutorialService
) : SplashTutorialRemoteDataSource {
    override suspend fun fetchSplashTutorialData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): SplashTutorialDto = withContext(Dispatchers.IO) {
        val response = apiService.fetchSplashTutorialRemoteData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
            currency = currency
        )
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("Request failed with code: ${response.code()}")
        }
    }
}