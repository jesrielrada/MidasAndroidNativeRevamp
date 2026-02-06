package com.prometheus_service.midas.core.di

import com.prometheus_service.midas.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Named("google_client_id")
    fun provideGoogleClientIdUat(): String = if (BuildConfig.BuildEnv == "U") BuildConfig.GoogleClientIdUAT else BuildConfig.GoogleClientIdPROD


}