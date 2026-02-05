package com.prometheus_service.midas.core.di

import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.SyncMultiLanguageData
import com.prometheus_service.midas.core.domain.shared.remote_config.use_case.GetRemoteConfig
import com.prometheus_service.midas.core.domain.shared.remote_domains.use_case.SyncRemoteDomains
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.SyncSplashTutorialImages
import com.prometheus_service.midas.core.domain.providers.CookieProvider
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchAppBaseUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.FormatGameUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetAccountLoggedInState
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetDomainFromUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.InitializeNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.SyncRemoteData
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

    @Provides
    @Singleton
    fun provideGetDomain(): GetDomainFromUrl {
        return GetDomainFromUrl()
    }

    @Provides
    @Singleton
    fun provideFormatGameUrl(): FormatGameUrl {
        return FormatGameUrl()
    }

    @Provides
    @Singleton
    fun provideGetAccountLoggedInState(
        cookieProvider: CookieProvider,
        getAppConfigModel: GetAppConfigModel,
        dispatcherProvider: DefaultDispatcherProvider
    ): GetAccountLoggedInState {
        return GetAccountLoggedInState(
            provider = cookieProvider,
            getAppConfigModel = getAppConfigModel,
            dispatcherProvider = dispatcherProvider
        )
    }


    @Provides
    @Singleton
    fun provideInitializeNativeCookies(
        cookieProvider: CookieProvider
    ): InitializeNativeCookies {
        return InitializeNativeCookies(cookieProvider)
    }

    @Provides
    @Singleton
    fun provideRemoteData(
        dispatcherProvider: DefaultDispatcherProvider,
        getAppConfig: GetAppConfigModel,
        syncRemoteDomains: SyncRemoteDomains,
        syncSplashTutorialImages: SyncSplashTutorialImages,
        syncMultiLanguageData: SyncMultiLanguageData
    ): SyncRemoteData {
        return SyncRemoteData(
            dispatcherProvider = dispatcherProvider,
            getAppConfig = getAppConfig,
            syncRemoteDomains = syncRemoteDomains,
            syncSplashTutorialImages = syncSplashTutorialImages,
            syncMultiLanguageData = syncMultiLanguageData
        )
    }
}