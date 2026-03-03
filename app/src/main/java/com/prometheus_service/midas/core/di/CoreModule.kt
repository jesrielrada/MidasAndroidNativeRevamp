package com.prometheus_service.midas.core.di

import android.content.Context
import com.prometheus_service.midas.core.data.manager.DefaultPermissionManager
import com.prometheus_service.midas.core.data.providers.DefaultDispatcherProvider
import com.prometheus_service.midas.core.domain.features.biometrics.manager.BiometricsManager
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.CacheSecondStageConfig
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.SyncMultiLanguageData
import com.prometheus_service.midas.core.domain.shared.remote_config.use_case.SyncRemoteConfig
import com.prometheus_service.midas.core.domain.shared.remote_domains.use_case.SyncRemoteDomains
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.SyncSplashTutorialImages
import com.prometheus_service.midas.core.domain.manager.PermissionManager
import com.prometheus_service.midas.core.domain.providers.CookieProvider
import com.prometheus_service.midas.core.domain.shared.app_config.AppConfigRepository
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.domain.shared.core.use_case.CacheAppCurrency
import com.prometheus_service.midas.core.domain.shared.core.use_case.CanDisplayMinimumOsDialog
import com.prometheus_service.midas.core.domain.shared.core.use_case.FetchRemoteConfig
import com.prometheus_service.midas.core.domain.shared.core.use_case.FormatGameUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetAccountLoggedInState
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetConfigDomains
import com.prometheus_service.midas.core.domain.shared.core.use_case.GetDomainFromUrl
import com.prometheus_service.midas.core.domain.shared.core.use_case.InitializeNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.InitializeUpdateVersionInfo
import com.prometheus_service.midas.core.domain.shared.core.use_case.PersistNativeCookies
import com.prometheus_service.midas.core.domain.shared.core.use_case.SyncRemoteData
import com.prometheus_service.midas.core.domain.shared.core.use_case.SetHostInterceptorUrl
import com.prometheus_service.midas.core.domain.shared.interceptors.HostInterceptor
import com.prometheus_service.midas.core.domain.shared.multi_language.MultiLanguageRepository
import com.prometheus_service.midas.core.domain.shared.multi_language.use_case.GetMultiLanguageData
import com.prometheus_service.midas.core.domain.shared.remote_domains.use_case.GetRemoteDomains
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun providePermissionManager(
        @ApplicationContext context: Context
    ): PermissionManager {
        return DefaultPermissionManager(context = context)
    }

    @Provides
    @Singleton
    fun provideInitializeVersionInfo() = InitializeUpdateVersionInfo()

    @Provides
    @Singleton
    fun provideCanDisplayMinimumOsDialog(
        appConfigRepository: AppConfigRepository,
        multiLanguageRepository: MultiLanguageRepository
    ): CanDisplayMinimumOsDialog {
        return CanDisplayMinimumOsDialog(
            appConfigRepository,
            multiLanguageRepository
        )
    }


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
    ): FetchRemoteConfig {
        return FetchRemoteConfig(
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