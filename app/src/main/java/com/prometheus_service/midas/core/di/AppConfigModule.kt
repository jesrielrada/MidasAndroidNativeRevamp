package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.features.app_config.DefaultAppConfigRepository
import com.prometheus_service.midas.core.data.features.app_config.local.AppConfigLocalDataSource
import com.prometheus_service.midas.core.data.features.app_config.local.DefaultAppConfigLocalDataSource
import com.prometheus_service.midas.core.data.features.app_config.remote.AppConfigRemoteDataSource
import com.prometheus_service.midas.core.data.features.app_config.remote.DefaultAppConfigRemoteDataSource
import com.prometheus_service.midas.core.data.features.app_config.util.serializer.AppConfigSerializer
import com.prometheus_service.midas.core.domain.features.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.features.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.features.app_config.use_case.GetApplicationConfig
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

    @Provides
    @Singleton
    fun provideGetApplicationConfig(
        repository: AppConfigRepository
    ): GetApplicationConfig {
        return GetApplicationConfig(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppConfigBindModule {

    @Binds
    @Singleton
    abstract fun bindAppConfigRepository(
        defaultRepository: DefaultAppConfigRepository
    ): AppConfigRepository

    @Binds
    @Singleton
    abstract fun bindAppConfigRemoteDataSource(
        defaultRemoteDataSource: DefaultAppConfigRemoteDataSource
    ): AppConfigRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAppConfigLocalDataSource(
        defaultLocalDataSource: DefaultAppConfigLocalDataSource
    ): AppConfigLocalDataSource

}