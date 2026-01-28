package com.prometheus_service.midas.core.di

import com.prometheus_service.midas.core.data.features.app_config.remote.AppConfigService
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
    ): AppConfigService {
        return retrofit.create(AppConfigService::class.java)
    }
}