package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.shared.remote_config.DefaultRemoteConfigRepository
import com.prometheus_service.midas.core.data.shared.remote_config.local.RemoteConfigLocalDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.local.DefaultRemoteConfigLocalDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.remote.RemoteConfigRemoteDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.remote.DefaultRemoteConfigRemoteDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.util.serializer.RemoteConfigSerializer
import com.prometheus_service.midas.core.domain.shared.remote_config.RemoteConfigRepository
import com.prometheus_service.midas.core.domain.shared.remote_config.model.RemoteConfigModel
import com.prometheus_service.midas.core.domain.shared.remote_config.use_case.GetRemoteConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {
    @Singleton
    @Provides
    fun provideRemoteConfigDataStore(
        @ApplicationContext context: Context
    ): DataStore<RemoteConfigModel> {
        return DataStoreFactory.create(
            serializer = RemoteConfigSerializer,
            produceFile = {
                context.dataStoreFile("remote_config_settings.json")
            }
        )
    }
    @Provides
    @Singleton
    fun provideGetRemoteConfig(
        repository: RemoteConfigRepository
    ): GetRemoteConfig {
        return GetRemoteConfig(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteConfigBindModule {

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRepository(
        defaultRepository: DefaultRemoteConfigRepository
    ): RemoteConfigRepository

    @Binds
    @Singleton
    abstract fun bindRemoteConfigRemoteDataSource(
        defaultRemoteDataSource: DefaultRemoteConfigRemoteDataSource
    ): RemoteConfigRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteConfigLocalDataSource(
        defaultLocalDataSource: DefaultRemoteConfigLocalDataSource
    ): RemoteConfigLocalDataSource

}