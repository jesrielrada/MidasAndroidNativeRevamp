package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.features.second_stage.local.DefaultSecondStageDataSource
import com.prometheus_service.midas.core.data.features.second_stage.local.SecondStageDataSource
import com.prometheus_service.midas.core.data.features.second_stage.repository.DefaultSecondStageRepository
import com.prometheus_service.midas.core.data.features.second_stage.util.serializer.SecondStageSerializer
import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import com.prometheus_service.midas.core.domain.features.second_stage.repository.SecondStageRepository
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.CacheSecondStageConfig
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.CanDisplayPinlock
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.DecryptPin
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.EncryptPin
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.GetSecondStageConfig
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.InitializeKey
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.InitializeVector
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecondStageModule {

    @Provides
    @Singleton
    fun provideCanDisplayPinlock(
        repository: SecondStageRepository
    ): CanDisplayPinlock {
        return CanDisplayPinlock(
            secondStageRepository = repository
        )
    }

    @Provides
    @Singleton
    fun provideCacheSecondStageConfig(
        repository: SecondStageRepository
    ): CacheSecondStageConfig {
        return CacheSecondStageConfig(
            repository = repository
        )
    }

    @Singleton
    @Provides
    fun provideGetSecondStageConfig(
        repository: SecondStageRepository
    ): GetSecondStageConfig {
        return GetSecondStageConfig(
            repository = repository
        )
    }

    @Singleton
    @Provides
    fun provideInitializeKeyUseCase(
        dispatcherProvider: DispatcherProvider
    ): InitializeKey {
        return InitializeKey(dispatcherProvider)
    }

    @Singleton
    @Provides
    fun provideInitializeVector(): InitializeVector {
        return InitializeVector()
    }

    @Singleton
    @Provides
    fun provideDecryptPin(
        initializeKey: InitializeKey,
        initializeVector: InitializeVector,
        dispatcherProvider: DispatcherProvider
    ): DecryptPin {
        return DecryptPin(
            initializeKey = initializeKey,
            initializeVector = initializeVector,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Singleton
    @Provides
    fun provideEncryptPin(
        initializeKey: InitializeKey,
        initializeVector: InitializeVector,
        dispatcherProvider: DispatcherProvider
    ): EncryptPin {
        return EncryptPin(
            initializeKey = initializeKey,
            initializeVector = initializeVector,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Singleton
    @Provides
    fun provideRemoteConfigDataStore(
        @ApplicationContext context: Context
    ): DataStore<SecondStageModel> {
        return DataStoreFactory.create(
            serializer = SecondStageSerializer,
            produceFile = {
                context.dataStoreFile("second_stage_settings.json")
            }
        )
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class SecondStageBindModule {
    @Binds
    @Singleton
    abstract fun bindSecondStageRepository(
        defaultRepository: DefaultSecondStageRepository
    ): SecondStageRepository


    @Binds
    @Singleton
    abstract fun bindSecondStageLocalDataSource(
        defaultDataSource: DefaultSecondStageDataSource
    ): SecondStageDataSource

}
