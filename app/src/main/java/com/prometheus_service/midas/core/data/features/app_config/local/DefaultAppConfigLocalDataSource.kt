package com.prometheus_service.midas.core.data.features.app_config.local

import androidx.datastore.core.DataStore
import com.prometheus_service.midas.core.domain.features.app_config.model.AppConfigModel
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

    override suspend fun cacheAppConfigModel(model: AppConfigModel) {
        dataStore.updateData {
            model
        }
    }

}