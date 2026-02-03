package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.shared.remote_domains.DefaultRemoteDomainsRepository
import com.prometheus_service.midas.core.data.shared.remote_domains.local.DefaultRemoteDomainsLocalDataSource
import com.prometheus_service.midas.core.data.shared.remote_domains.local.RemoteDomainsLocalDataSource
import com.prometheus_service.midas.core.data.shared.remote_domains.remote.DefaultRemoteDomainsRemoteDataSource
import com.prometheus_service.midas.core.data.shared.remote_domains.remote.RemoteDomainsRemoteDataSource
import com.prometheus_service.midas.core.data.shared.remote_domains.util.serializer.RemoteDomainsSerializer
import com.prometheus_service.midas.core.domain.shared.remote_domains.RemoteDomainsRepository
import com.prometheus_service.midas.core.domain.shared.remote_domains.model.RemoteDomainsModel
import com.prometheus_service.midas.core.domain.shared.remote_domains.use_case.SyncRemoteDomains
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDomainsModule {
    @Singleton
    @Provides
    fun provideRemoteDomainsDataStore(
        @ApplicationContext context: Context
    ): DataStore<RemoteDomainsModel> {
        return DataStoreFactory.create(
            serializer = RemoteDomainsSerializer,
            produceFile = {
                context.dataStoreFile("remote_domains_settings.json")
            }
        )
    }

    @Provides
    @Singleton
    fun provideGetRemoteDomainsFromRemote(
        repository: RemoteDomainsRepository
    ): SyncRemoteDomains {
        return SyncRemoteDomains(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDomainsBindModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDomainsRepository(
        defaultRepository: DefaultRemoteDomainsRepository
    ): RemoteDomainsRepository

    @Binds
    @Singleton
    abstract fun bindRemoteDomainsRemoteDataSource(
        defaultRemoteDataSource: DefaultRemoteDomainsRemoteDataSource
    ): RemoteDomainsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteDomainsLocalDataSource(
        defaultLocalDataSource: DefaultRemoteDomainsLocalDataSource
    ): RemoteDomainsLocalDataSource
}


