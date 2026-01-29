package com.prometheus_service.midas.core.data.features.multi_language.remote

import com.prometheus_service.midas.core.data.features.multi_language.remote.model.MultiLanguageDto
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultMultiLanguageRemoteRemoteDataSource @Inject constructor(
    private val apiService: MultiLanguageService,
    private val dispatcherProvider: DefaultDispatcherProvider
) : MultiLanguageRemoteDataSource {
    override suspend fun fetchMultiLanguageSupport(
        operatorId: String,
        userAgent: String,
        acceptLanguage: String,
        currency: String?
    ): MultiLanguageDto = withContext(dispatcherProvider.io) {
        val response = apiService.fetchMultiLanguageSupport(
            operatorId = operatorId,
            userAgent = userAgent,
            language = acceptLanguage,
            currency = currency
        )
        if (response.isSuccessful) {
            val body = response.body()
            Timber.d("Successfully fetched multi-language data with body $body")
            body ?: throw Exception("Response body is null")
        } else {
            throw Exception("Request failed with code: ${response.code()}")
        }
    }
}