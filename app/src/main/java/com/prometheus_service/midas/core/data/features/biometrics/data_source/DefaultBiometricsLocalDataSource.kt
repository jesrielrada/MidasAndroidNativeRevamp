package com.prometheus_service.midas.core.data.features.biometrics.data_source

import androidx.datastore.core.DataStore
import com.prometheus_service.midas.core.domain.features.biometrics.model.BiometricsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class DefaultBiometricsLocalDataSource @Inject constructor(
    private val dataStore: DataStore<BiometricsModel>
) : BiometricsLocalDataSource {


    override val biometricsModel: Flow<BiometricsModel> =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading BiometricsModel DataStore")
                emit(BiometricsModel()) // Fallback to default
            } else {
                throw exception
            }
        }

    override suspend fun cacheBiometricsModel(model: BiometricsModel) {
        try {
            dataStore.updateData { currentData ->
                currentData.copy(
                    isBiometricsEnabled = model.isBiometricsEnabled
                        ?: currentData.isBiometricsEnabled,
                    isCmsboEnabled = model.isCmsboEnabled ?: currentData.isCmsboEnabled,
                    usernames = model.usernames ?: currentData.usernames,
                    cipherList = model.cipherList ?: currentData.cipherList,
                    currentAccount = model.currentAccount ?: currentData.currentAccount
                )
            }
        } catch (e: Exception) {
            Timber.d("Failed to cache BiometricsModel, $e")
        }
    }
}