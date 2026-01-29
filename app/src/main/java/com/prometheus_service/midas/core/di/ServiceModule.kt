package com.prometheus_service.midas.core.di

import com.prometheus_service.midas.core.data.features.remote_config.remote.RemoteConfigService
import com.prometheus_service.midas.core.data.features.multi_language.remote.MultiLanguageService
import com.prometheus_service.midas.core.data.features.remote_domains.remote.RemoteDomainsService
import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.SplashTutorialService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideSplashTutorialService(
        retrofit: Retrofit
    ): SplashTutorialService {
        return retrofit.create(SplashTutorialService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppConfigService(
        retrofit: Retrofit
    ): RemoteConfigService {
        return retrofit.create(RemoteConfigService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDomainsService(
        retrofit: Retrofit
    ): RemoteDomainsService {
        return retrofit.create(RemoteDomainsService::class.java)
    }

    @Provides
    @Singleton
    fun provideMultiLanguageService(
        retrofit: Retrofit
    ): MultiLanguageService {
        return retrofit.create(MultiLanguageService::class.java)
    }
}