package com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case

import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.features.splash_tutorial.SplashTutorialRepository
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class GetSplashImages @Inject constructor(
    private val repository: SplashTutorialRepository,
    private val dispatcherProvider: DefaultDispatcherProvider
) {
    operator fun invoke(): Flow<SplashTutorialModel> = repository.splashTutorialModel
        .flowOn(dispatcherProvider.io)
        .catch { e ->
            Timber.d("Error getting splash images: $e")
            throw e
        }
}