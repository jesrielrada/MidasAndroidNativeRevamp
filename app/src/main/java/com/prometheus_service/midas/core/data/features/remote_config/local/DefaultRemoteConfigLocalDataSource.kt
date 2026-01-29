package com.prometheus_service.midas.core.data.features.remote_config.local

import androidx.datastore.core.DataStore
import com.prometheus_service.midas.core.domain.features.remote_config.model.RemoteConfigModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class DefaultRemoteConfigLocalDataSource @Inject constructor(
    private val dataStore: DataStore<RemoteConfigModel>
) : RemoteConfigLocalDataSource {

    private val remoteConfigFlow: Flow<RemoteConfigModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading RemoteConfig DataStore")
                emit(RemoteConfigModel()) // Fallback to default
            } else {
                throw exception
            }
        }

    override fun getRemoteConfigModel(): Flow<RemoteConfigModel> {
        return remoteConfigFlow
    }

    override suspend fun cacheRemoteConfigModel(model: RemoteConfigModel) {
        dataStore.updateData {
            model
        }
    }

}