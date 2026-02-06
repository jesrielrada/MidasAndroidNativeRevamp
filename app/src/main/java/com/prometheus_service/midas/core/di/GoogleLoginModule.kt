package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.prometheus_service.midas.core.domain.features.google_login.use_cases.GetGoogleAuthUrl
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleLoginModule {

    @Provides
    @Singleton
    fun providesCredentialManager(
        @ApplicationContext context: Context
    ): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun provideGetGoogleAuthUrl(
        dispatcherProvider: DispatcherProvider
    ): GetGoogleAuthUrl {
        return GetGoogleAuthUrl(
            dispatcherProvider = dispatcherProvider,
        )
    }

}