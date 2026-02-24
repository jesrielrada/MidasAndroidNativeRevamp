package com.prometheus_service.midas.core.data.shared.app_config

import android.annotation.SuppressLint
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.data.shared.app_config.local.AppConfigLocalDataSource
import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import timber.log.Timber
import javax.inject.Inject

class DefaultAppConfigRepository @Inject constructor(
    private val localDataSource: AppConfigLocalDataSource,
    private val dispatcherProvider: DefaultDispatcherProvider,
    private val json: Json
) : AppConfigRepository {

    override val appConfigModel: Flow<AppConfigModel>
        get() = localDataSource.getAppConfigModel().flowOn(dispatcherProvider.io)

    override suspend fun cacheAppConfigModel(model: AppConfigModel): Result<Unit> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                localDataSource.cacheAppConfigModel(model)
            }.onFailure { exception ->
                Timber.e(exception, "Failed to cache app config model")
            }
        }
    }

    @SuppressLint("BinaryOperationInTimber")
    override suspend fun cacheAppCurrency(data: String) {
        withContext(dispatcherProvider.io) {
            runCatching {
                val json = json.parseToJsonElement(data).jsonObject
                val isMultiCurrencyEnabled = json["isMultiCurrency"]?.jsonPrimitive?.boolean

                if (isMultiCurrencyEnabled == true) {
                    val defaultCurrency = json["defaultCurrency"]
                        ?.takeIf { it is JsonPrimitive }
                        ?.jsonPrimitive
                        ?.contentOrNull

                    val memberCurrency = json["user"]
                        ?.takeIf { it is JsonObject }
                        ?.jsonObject
                        ?.get("currency")
                        ?.takeIf { it is JsonPrimitive }
                        ?.jsonPrimitive
                        ?.contentOrNull

                    Timber.d(
                        "Caching app currency ... " +
                                "default currency: $defaultCurrency" +
                                " member currency: $memberCurrency"
                    )

                    val currency = memberCurrency ?: defaultCurrency

                    localDataSource.cacheAppConfigModel(
                        AppConfigModel(
                            currency = currency
                        )
                    )
                }
            }.onFailure {
                Timber.d(it, "Failed to cache app currency ...")
            }

        }
    }

    override suspend fun deleteCookies() {
        withContext(dispatcherProvider.io) {
            runCatching {
                localDataSource.deleteSessionCookies()
            }.onFailure { exception ->
                Timber.e(exception, "Failed to delete session cookies")
            }
        }
    }

    override suspend fun deleteBestDomain() {
        withContext(dispatcherProvider.io) {
            runCatching {
                localDataSource.deleteBestDomain()
            }.onFailure { exception ->
                Timber.e(exception, "Failed to delete best domain")
            }
        }
    }
}
