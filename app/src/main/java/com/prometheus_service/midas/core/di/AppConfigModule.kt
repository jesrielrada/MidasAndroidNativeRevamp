package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.features.remote_config.DefaultRemoteConfigRepository
import com.prometheus_service.midas.core.data.features.remote_config.local.RemoteConfigLocalDataSource
import com.prometheus_service.midas.core.data.features.remote_config.local.DefaultRemoteConfigLocalDataSource
import com.prometheus_service.midas.core.data.features.remote_config.remote.RemoteConfigRemoteDataSource
import com.prometheus_service.midas.core.data.features.remote_config.remote.DefaultRemoteConfigRemoteDataSource
import com.prometheus_service.midas.core.data.features.remote_config.util.serializer.RemoteConfigSerializer
import com.prometheus_service.midas.core.domain.features.remote_config.RenameConfigRepository
import com.prometheus_service.midas.core.domain.features.remote_config.model.RemoteConfigModel
import com.prometheus_service.midas.core.domain.features.remote_config.use_case.GetRemoteConfig
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
    ): DataStore<RemoteConfigModel> {
        return DataStoreFactory.create(
            serializer = RemoteConfigSerializer,
            produceFile = {
                context.dataStoreFile("app_config_settings.json")
            }
        )
    }

    @Provides
    @Singleton
    fun provideGetApplicationConfig(
        repository: RenameConfigRepository
    ): GetRemoteConfig {
        return GetRemoteConfig(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppConfigBindModule {

    @Binds
    @Singleton
    abstract fun bindAppConfigRepository(
        defaultRepository: DefaultRemoteConfigRepository
    ): RenameConfigRepository

    @Binds
    @Singleton
    abstract fun bindAppConfigRemoteDataSource(
        defaultRemoteDataSource: DefaultRemoteConfigRemoteDataSource
    ): RemoteConfigRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAppConfigLocalDataSource(
        defaultLocalDataSource: DefaultRemoteConfigLocalDataSource
    ): RemoteConfigLocalDataSource

}