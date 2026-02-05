package com.prometheus_service.midas.core.di

import com.prometheus_service.midas.core.data.providers.DefaultCookieProvider
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.providers.CookieProvider
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

    @Provides
    @Singleton
    fun provideCookieProvider(): CookieProvider {
        return DefaultCookieProvider()
    }
}