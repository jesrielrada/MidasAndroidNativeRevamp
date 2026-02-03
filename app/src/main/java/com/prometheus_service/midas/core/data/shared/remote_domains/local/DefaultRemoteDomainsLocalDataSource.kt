package com.prometheus_service.midas.core.data.shared.remote_domains.local

import androidx.datastore.core.DataStore
import com.prometheus_service.midas.core.domain.shared.remote_domains.model.RemoteDomainsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class DefaultRemoteDomainsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<RemoteDomainsModel>
) : RemoteDomainsLocalDataSource {

    private val remoteDomainsFlow: Flow<RemoteDomainsModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading Remote Domains DataStore")
                emit(RemoteDomainsModel()) // Fallback to default
            } else {
                throw exception
            }
        }

    override fun getRemoteDomainsModel(): Flow<RemoteDomainsModel> {
        return remoteDomainsFlow
    }


    override suspend fun cacheRemoteDomainsModel(data: RemoteDomainsModel) {
        dataStore.updateData { data }
    }
}