package com.prometheus_service.midas.core.data.shared.app_config.local

import androidx.datastore.core.DataStore
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class DefaultAppConfigLocalDataSource @Inject constructor(
    private val dataStore: DataStore<AppConfigModel>
) : AppConfigLocalDataSource {

    private val appConfigFlow: Flow<AppConfigModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading AppConfig DataStore")
                emit(AppConfigModel()) // Fallback to default
            } else {
                throw exception
            }
        }

    override fun getAppConfigModel(): Flow<AppConfigModel> {
        return appConfigFlow
    }

    override suspend fun cacheAppConfigModel(data: AppConfigModel) {
        dataStore.updateData { currentData ->
            currentData.copy(
                version = data.version ?: currentData.version,
                bestDomain = data.bestDomain ?: currentData.bestDomain,
                locale = data.locale ?: currentData.locale,
                currency = data.currency ?: currentData.currency,
                baseUrl = data.baseUrl ?: currentData.baseUrl,
                isTutorialDisplayed = data.isTutorialDisplayed ?: currentData.isTutorialDisplayed,
                isLanguageSelectionDisplayed = data.isLanguageSelectionDisplayed
                    ?: currentData.isLanguageSelectionDisplayed,
                domain = data.domain ?: currentData.domain,
                sessionCookies = data.sessionCookies ?: currentData.sessionCookies
            )
        }
    }

    override suspend fun deleteSessionCookies() {
        dataStore.updateData {
            it.copy(
                sessionCookies = null
            )
        }
    }

    override suspend fun deleteBestDomain() {
        dataStore.updateData {
            it.copy(
                bestDomain = null
            )
        }
    }
}