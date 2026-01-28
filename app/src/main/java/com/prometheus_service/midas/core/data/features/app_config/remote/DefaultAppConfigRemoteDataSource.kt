package com.prometheus_service.midas.core.data.features.app_config.remote

import com.prometheus_service.midas.core.data.features.app_config.remote.model.AppConfigDto
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultAppConfigRemoteDataSource @Inject constructor(
    private val apiService: AppConfigService,
    private val dispatcherProvider: DefaultDispatcherProvider
) : AppConfigRemoteDataSource {

    override suspend fun fetchApplicationConfig(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String
    ): AppConfigDto = withContext(dispatcherProvider.io){
        val response = apiService.fetchApplicationConfigRemoteData(
            operatorId = operatorId,
            userAgent = userAgent,
            acceptLanguage = acceptLanguage,
        )
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body is null")
        } else {
            throw Exception("Request failed with code: ${response.code()}")
        }
    }
}