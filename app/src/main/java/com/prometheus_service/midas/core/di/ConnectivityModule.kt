package com.prometheus_service.midas.core.di

import android.content.Context
import com.prometheus_service.midas.core.data.shared.connectivity.DefaultConnectivityRepository
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import com.prometheus_service.midas.core.domain.shared.connectivity.ConnectivityRepository
import com.prometheus_service.midas.core.domain.shared.connectivity.use_case.GetNetworkType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityRepository(
        @ApplicationContext context: Context
    ): ConnectivityRepository {
        return DefaultConnectivityRepository(context)
    }

    @Provides
    @Singleton
    fun provideFetchNetworkType(
        repository: ConnectivityRepository
    ): GetNetworkType {
        return GetNetworkType(
            repository
        )
    }

}