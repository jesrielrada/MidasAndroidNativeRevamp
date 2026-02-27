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
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.GetBiometricCurrentAccount
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleAccountDeletion
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleAccountSelectedAuthSucceed
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricAccountDisplay
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricAccountSelected
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricAuthCancelled
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricAuthError
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.HandleBiometricsEnrollment
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.InitializeBiometricsPrompt
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.PersistBiometricsUser
import com.prometheus_service.midas.core.domain.features.biometrics.use_case.SetBiometricsEnabled
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BiometricsModule {

    @Provides
    @Singleton
    fun provideHandleAccountDeletion(
        manager: BiometricsManager
    ): HandleAccountDeletion {
        return HandleAccountDeletion(
            biometricsManager = manager
        )
    }

    @Provides
    @Singleton
    fun provideHandleBiometricAuthDismissed(
        biometricsManager: BiometricsManager,
        setBiometricsEnabled: SetBiometricsEnabled
    ): HandleBiometricAuthCancelled {
        return HandleBiometricAuthCancelled(
            biometricManager = biometricsManager,
            setBiometricsEnabled = setBiometricsEnabled
        )
    }

    @Singleton
    @Provides
    fun provideHandleBiometricAuthError(
        biometricsManager: BiometricsManager,
        setBiometricsEnabled: SetBiometricsEnabled
    ): HandleBiometricAuthError {
        return HandleBiometricAuthError(
            biometricsManager = biometricsManager,
            setBiometricsEnabled = setBiometricsEnabled
        )
    }

    @Singleton
    @Provides
    fun provideGetBiometricCurrentAccount(
        biometricsManager: BiometricsManager
    ): GetBiometricCurrentAccount {
        return GetBiometricCurrentAccount(
            biometricsManager = biometricsManager
        )
    }

    @Singleton
    @Provides
    fun provideHandleAccountSelectedAuthSucceeded(
        cipherManager: CipherManager,
        biometricsManager: BiometricsManager
    ): HandleAccountSelectedAuthSucceed {
        return HandleAccountSelectedAuthSucceed(
            cipherManager = cipherManager,
            biometricsManager = biometricsManager
        )
    }

    @Singleton
    @Provides
    fun provideHandleBiometricAccountSelected(
        biometricsManager: BiometricsManager,
        cipherManager: CipherManager,
        setBiometricsEnabled: SetBiometricsEnabled
    ): HandleBiometricAccountSelected {
        return HandleBiometricAccountSelected(
            biometricsManager = biometricsManager,
            cipherManager = cipherManager,
            setBiometricsEnabled = setBiometricsEnabled
        )
    }


    @Singleton
    @Provides
    fun provideHandleBiometricAccountDisplay(
        biometricsManager: BiometricsManager,
        setBiometricsEnabled: SetBiometricsEnabled
    ): HandleBiometricAccountDisplay {
        return HandleBiometricAccountDisplay(
            biometricsManager = biometricsManager,
            setBiometricsEnabled = setBiometricsEnabled
        )
    }

    @Singleton
    @Provides
    fun providePersistBiometricsUser(
        biometricsManager: BiometricsManager,
        cipherManager: CipherManager
    ): PersistBiometricsUser {
        return PersistBiometricsUser(
            biometricsManager = biometricsManager,
            cipherManager = cipherManager
        )
    }

    @Singleton
    @Provides
    fun provideSetBiometricsEnabled(
        repository: BiometricsRepository,
        getMultiLanguageData: GetMultiLanguageData
    ): SetBiometricsEnabled {
        return SetBiometricsEnabled(
            repository = repository,
            getMultiLanguageData = getMultiLanguageData
        )
    }

    @Singleton
    @Provides
    fun provideInitializeBiometricsPrompt(
        cipherManager: CipherManager,
        biometricsManager: BiometricsManager
    ): InitializeBiometricsPrompt {
        return InitializeBiometricsPrompt(
            cipherManager = cipherManager,
            biometricsManager = biometricsManager
        )
    }

    @Singleton
    @Provides
    fun provideHandleBiometricEnrollment(
        biometricsManager: BiometricsManager,
        cipherManager: CipherManager
    ): HandleBiometricsEnrollment {
        return HandleBiometricsEnrollment(
            biometricsManager = biometricsManager,
            cipherManager = cipherManager
        )
    }

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


