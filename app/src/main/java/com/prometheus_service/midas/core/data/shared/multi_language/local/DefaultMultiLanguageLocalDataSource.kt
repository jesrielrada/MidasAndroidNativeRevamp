package com.prometheus_service.midas.core.data.shared.multi_language.local

import androidx.datastore.core.DataStore
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedModels
import com.prometheus_service.midas.core.domain.shared.multi_language.model.MultiLanguageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class DefaultMultiLanguageLocalDataSource @Inject constructor(
    private val dataStore: DataStore<MultiLanguageModel>
) : MultiLanguageLocalDataSource {

    private val multiLanguageFlow: Flow<MultiLanguageModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading MultiLanguage DataStore")
                emit(MultiLanguageModel()) // Fallback to default
            } else {
                throw exception
            }
        }

    override fun getLocalizedLanguageModel(locale: String): Flow<LocalizedModels?> {
        return multiLanguageFlow.map { allData ->
            allData.localizedModels[locale]
        }
    }

    override suspend fun cacheMultiLanguageModel(model: MultiLanguageModel) {
        try {
            dataStore.updateData { current ->
                val mergedMap = current.localizedModels.toMutableMap().apply {
                    putAll(model.localizedModels)
                }
                current.copy(localizedModels = mergedMap)
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to update MultiLanguage DataStore")
        }
    }
}