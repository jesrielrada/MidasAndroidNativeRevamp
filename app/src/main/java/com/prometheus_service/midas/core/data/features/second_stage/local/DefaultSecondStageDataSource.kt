package com.prometheus_service.midas.core.data.features.second_stage.local

import androidx.datastore.core.DataStore
import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


class DefaultSecondStageDataSource @Inject constructor(
    private val dataStore: DataStore<SecondStageModel>
) : SecondStageDataSource {

    private val secondStageFlow: Flow<SecondStageModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading SecondStage DataStore")
                emit(SecondStageModel()) // Fallback to default
            } else {
                throw exception
            }
        }

    override fun getSecondStageModel(): Flow<SecondStageModel> {
        return secondStageFlow
    }

    override suspend fun cacheSecondStageModel(data: SecondStageModel) {
        try {
            dataStore.updateData { currentData ->
                currentData.copy(
                    isUserEnabled = data.isUserEnabled ?: currentData.isUserEnabled,
                    isCmsboEnabled = data.isCmsboEnabled ?: currentData.isCmsboEnabled,
                    pin = data.pin ?: currentData.pin,
                    credentials = data.credentials ?: currentData.credentials
                )
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to cache second stage model")
        }
    }
}