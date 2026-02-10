package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.features.biometrics.data_source.BiometricsLocalDataSource
import com.prometheus_service.midas.core.data.features.biometrics.data_source.DefaultBiometricsLocalDataSource
import com.prometheus_service.midas.core.data.features.biometrics.manager.DefaultBiometricManager
import com.prometheus_service.midas.core.data.features.biometrics.manager.DefaultCipherManager
import com.prometheus_service.midas.core.data.features.biometrics.repository.DefaultBiometricsRepository
import com.prometheus_service.midas.core.data.providers.util.BiometricsSerializer
import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.BiometricsModel
import com.prometheus_service.midas.core.domain.features.biometrics.repository.BiometricsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BiometricsModule {

    @Singleton
    @Provides
    fun provideBiometricsDataStore(
        @ApplicationContext context: Context
    ): DataStore<BiometricsModel> {
        return DataStoreFactory.create(
            serializer = BiometricsSerializer,
            produceFile = {
                context.dataStoreFile("biometrics_settings.json")
            }
        )
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideCipherManager(): CipherManager {
        return DefaultCipherManager()
    }

    @Provides
    @Singleton
    fun provideBiometricsManager(
        @ApplicationContext context: Context,
        repository: BiometricsRepository
    ): BiometricsManager {
        return DefaultBiometricManager(
            context = context,
            repository = repository
        )
    }

}


@Module
@InstallIn(SingletonComponent::class)
abstract class BiometricsBindModule {


    @Binds
    @Singleton
    abstract fun bindBiometricsRepository(
        defaultBiometricsRepository: DefaultBiometricsRepository
    ): BiometricsRepository

    @Binds
    @Singleton
    abstract fun bindBiometricsLocalDataSource(
        defaultBiometricsLocalDataSource: DefaultBiometricsLocalDataSource
    ): BiometricsLocalDataSource

}


