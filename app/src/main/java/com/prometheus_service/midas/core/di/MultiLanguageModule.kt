package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.features.multi_language.DefaultMultiLanguageRepository
import com.prometheus_service.midas.core.data.features.multi_language.local.DefaultMultiLanguageLocalDataSource
import com.prometheus_service.midas.core.data.features.multi_language.local.MultiLanguageLocalDataSource
import com.prometheus_service.midas.core.data.features.multi_language.remote.DefaultMultiLanguageRemoteRemoteDataSource
import com.prometheus_service.midas.core.data.features.multi_language.remote.MultiLanguageRemoteDataSource
import com.prometheus_service.midas.core.data.features.multi_language.util.serializer.MultiLanguageSerializer
import com.prometheus_service.midas.core.domain.features.multi_language.MultiLanguageRepository
import com.prometheus_service.midas.core.domain.features.multi_language.model.MultiLanguageModel
import com.prometheus_service.midas.core.domain.features.multi_language.use_case.GetMultiLanguageData
import com.prometheus_service.midas.core.domain.features.multi_language.use_case.RefreshMultiLanguageData
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MultiLanguageModule {
    @Provides
    @Singleton
    fun provideAppConfigDataStore(
        @ApplicationContext context: Context
    ): DataStore<MultiLanguageModel> {
        return DataStoreFactory.create(
            serializer = MultiLanguageSerializer,
            produceFile = {
                context.dataStoreFile("multi_language_settings.json")
            }
        )
    }
    @Provides
    @Singleton
    fun provideGetMultiLanguageRemoteData(
        repository: MultiLanguageRepository
    ): RefreshMultiLanguageData {
        return RefreshMultiLanguageData(repository)
    }
    @Provides
    @Singleton
    fun provideGetMultiLanguageData(
        repository: MultiLanguageRepository
    ): GetMultiLanguageData {
        return GetMultiLanguageData(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MultiLanguageBindModule {
    @Binds
    @Singleton
    abstract fun bindMultiLanguageRemoteDataSource(
        defaultRemoteDataSource: DefaultMultiLanguageRemoteRemoteDataSource
    ): MultiLanguageRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMultiLanguageLocalDataSource(
        defaultLocalDataSource: DefaultMultiLanguageLocalDataSource
    ): MultiLanguageLocalDataSource

    @Binds
    abstract fun bindMultiLanguageRepository(
        defaultRepository: DefaultMultiLanguageRepository
    ): MultiLanguageRepository
}