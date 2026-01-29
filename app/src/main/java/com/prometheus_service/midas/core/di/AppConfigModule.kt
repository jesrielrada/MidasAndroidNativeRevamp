package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.shared.app_config.DefaultAppConfigRepository
import com.prometheus_service.midas.core.data.shared.app_config.local.AppConfigLocalDataSource
import com.prometheus_service.midas.core.data.shared.app_config.local.DefaultAppConfigLocalDataSource
import com.prometheus_service.midas.core.data.shared.app_config.util.serializer.AppConfigSerializer
import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetConfigModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Singleton
    @Provides
    fun provideAppConfigDataStore(
        @ApplicationContext context: Context
    ): DataStore<AppConfigModel> {
        return DataStoreFactory.create(
            serializer = AppConfigSerializer,
            produceFile = {
                context.dataStoreFile("app_config_settings.json")
            }
        )
    }

    @Singleton
    @Provides
    fun provideCacheAppConfigModel(
        repository: AppConfigRepository
    ): CacheAppConfigModel {
        return CacheAppConfigModel(repository)
    }

    @Singleton
    @Provides
    fun provideGetConfigModel(
        repository: AppConfigRepository
    ): GetConfigModel {
        return GetConfigModel(repository)
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class AppConfigBindModule {

    @Binds
    @Singleton
    abstract fun bindAppConfigLocalDataSource(
        defaultAppConfigLocalDataSource: DefaultAppConfigLocalDataSource
    ): AppConfigLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAppConfigRepository(
        defaultAppConfigRepository: DefaultAppConfigRepository
    ): AppConfigRepository

}