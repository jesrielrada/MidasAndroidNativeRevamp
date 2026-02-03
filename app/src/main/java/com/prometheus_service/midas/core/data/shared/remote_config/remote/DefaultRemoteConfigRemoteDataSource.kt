package com.prometheus_service.midas.core.data.shared.remote_config.remote

import com.prometheus_service.midas.core.data.shared.remote_config.remote.model.RemoteConfigDto
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultRemoteConfigRemoteDataSource @Inject constructor(
    private val apiService: RemoteConfigService,
    private val dispatcherProvider: DefaultDispatcherProvider
) : RemoteConfigRemoteDataSource {

    override suspend fun fetchRemoteConfig(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String
    ): RemoteConfigDto = withContext(dispatcherProvider.io){
        val response = apiService.fetchRemoteConfigRemoteData(
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