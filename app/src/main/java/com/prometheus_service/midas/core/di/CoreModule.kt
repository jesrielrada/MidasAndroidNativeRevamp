package com.prometheus_service.midas.core.di

import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.CacheSecondStageConfig
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.SyncMultiLanguageData
import com.prometheus_service.midas.core.domain.shared.remote_config.use_case.SyncRemoteConfig
import com.prometheus_service.midas.core.domain.shared.remote_domains.use_case.SyncRemoteDomains
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.SyncSplashTutorialImages
import com.prometheus_service.midas.core.domain.providers.CookieProvider
import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.shared.core.use_case.CacheAppCurrency
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchAppBaseUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.FormatGameUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetAccountLoggedInState
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetConfigDomains
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetDomainFromUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.InitializeNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.PersistNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.SyncRemoteData
import com.prometheus_service.midas.core.domain.shared.core.use_case.SetHostInterceptorUrl
import com.prometheus_service.midas.core.domain.shared.interceptors.HostInterceptor
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import com.prometheus_service.midas.core.domain.shared.remote_domains.use_case.GetRemoteDomains
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideGetConfigDomains(
        remoteDomains: GetRemoteDomains,
        getAppConfigModel: GetAppConfigModel
    ): GetConfigDomains {
        return GetConfigDomains(
            remoteDomains,
            getAppConfigModel
        )
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providePersistNativeCookies(
        cookieProvider: CookieProvider
    ): PersistNativeCookies {
        return PersistNativeCookies(cookieProvider)
    }


    @Provides
    @Singleton
    fun provideCacheAppCurrency(
        appConfigRepository: AppConfigRepository
    ): CacheAppCurrency {
        return CacheAppCurrency(
            appConfigRepository = appConfigRepository
        )
    }

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
        syncRemoteConfig: SyncRemoteConfig,
        setHostInterceptorUrl: SetHostInterceptorUrl,
        dispatcherProvider: DefaultDispatcherProvider
    ): FetchAppBaseUrl {
        return FetchAppBaseUrl(
            getAppConfig = getAppConfig,
            syncRemoteConfig = syncRemoteConfig,
            setHostInterceptorUrl = setHostInterceptorUrl,
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
        cookieProvider: CookieProvider,
        getAppConfigModel: GetAppConfigModel
    ): InitializeNativeCookies {
        return InitializeNativeCookies(
            cookieProvider,
            getAppConfigModel
        )
    }

    @Provides
    @Singleton
    fun provideRemoteData(
        dispatcherProvider: DefaultDispatcherProvider,
        getAppConfig: GetAppConfigModel,
        syncRemoteDomains: SyncRemoteDomains,
        syncSplashTutorialImages: SyncSplashTutorialImages,
        syncMultiLanguageData: SyncMultiLanguageData,
        cacheSecondStageConfig: CacheSecondStageConfig,
        biometricsManager: BiometricsManager,
        getMultiLanguageData: GetMultiLanguageData
    ): SyncRemoteData {
        return SyncRemoteData(
            dispatcherProvider = dispatcherProvider,
            getAppConfig = getAppConfig,
            syncRemoteDomains = syncRemoteDomains,
            syncSplashTutorialImages = syncSplashTutorialImages,
            syncMultiLanguageData = syncMultiLanguageData,
            cacheSecondStageConfig = cacheSecondStageConfig,
            biometricsManager = biometricsManager,
            getMultiLanguageData = getMultiLanguageData
        )
    }
}