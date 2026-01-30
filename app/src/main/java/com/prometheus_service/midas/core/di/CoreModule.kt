package com.prometheus_service.midas.core.di

import com.google.gson.annotations.Since
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.features.remote_config.use_case.GetRemoteConfig
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchAppBaseUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.SetHostInterceptorUrl
import com.prometheus_service.midas.core.domain.shared.interceptors.HostInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    @Provides
    @Singleton
    fun provideSetInterceptorUrl(
        hostInterceptor: HostInterceptor
    ): SetHostInterceptorUrl {
        return SetHostInterceptorUrl(hostInterceptor)
    }

    @Provides
    @Singleton
    fun provideFetchBaseUrl(
        getAppConfig: GetAppConfigModel,
        getRemoteConfig: GetRemoteConfig,
        dispatcherProvider: DefaultDispatcherProvider
    ): FetchAppBaseUrl {
        return FetchAppBaseUrl(
            getAppConfig = getAppConfig,
            getRemoteConfig = getRemoteConfig,
            dispatcherProvider = dispatcherProvider
        )
    }
}