package com.prometheus_service.midas.core.data.features.remote_domains.remote

import com.prometheus_service.midas.core.data.features.remote_domains.remote.model.RemoteDomainsDto
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultRemoteDomainsRemoteDataSource @Inject constructor(
    private val apiService: RemoteDomainsService,
    private val dispatcherProvider: DefaultDispatcherProvider
) : RemoteDomainsRemoteDataSource {

    override suspend fun fetchRemoteDomainsData(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): RemoteDomainsDto = withContext(dispatcherProvider.io) {
        val response = apiService.fetchRemoteDomainsData(
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