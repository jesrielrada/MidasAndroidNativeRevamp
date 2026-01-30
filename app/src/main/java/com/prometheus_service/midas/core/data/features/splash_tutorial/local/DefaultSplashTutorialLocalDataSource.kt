package com.prometheus_service.midas.core.data.features.splash_tutorial.local

import androidx.datastore.core.DataStore
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class DefaultSplashTutorialLocalDataSource @Inject constructor(
    private val dataStore: DataStore<SplashTutorialModel>
) : SplashTutorialLocalDataSource {

    private val splashTutorialFlow: Flow<SplashTutorialModel> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception, "Error reading SplashTutorial DataStore")
                emit(SplashTutorialModel()) // Fallback to default
            } else {
                throw exception
            }
        }

    override fun getSplashTutorialModel(): Flow<SplashTutorialModel> {
        return splashTutorialFlow
    }

    override suspend fun cacheSplashTutorialModel(data: SplashTutorialModel) {
        dataStore.updateData {
            data
        }
    }
}