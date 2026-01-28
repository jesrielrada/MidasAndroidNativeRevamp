package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.features.splash_tutorial.DefaultSplashTutorialRepository
import com.prometheus_service.midas.core.data.features.splash_tutorial.local.DefaultSplashTutorialLocalDataSource
import com.prometheus_service.midas.core.data.features.splash_tutorial.local.SplashTutorialLocalDataSource
import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.DefaultSplashTutorialRemoteDataSource
import com.prometheus_service.midas.core.data.features.splash_tutorial.remote.SplashTutorialRemoteDataSource
import com.prometheus_service.midas.core.data.features.splash_tutorial.utils.serializer.SplashTutorialSerializer
import com.prometheus_service.midas.core.domain.features.splash_tutorial.SplashTutorialRepository
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.GetSplashTutorialImages
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SplashTutorialModule {
    @Singleton
    @Provides
    fun provideSplashTutorialDataStore(
        @ApplicationContext context: Context
    ): DataStore<SplashTutorialModel> {
        return DataStoreFactory.create(
            serializer = SplashTutorialSerializer,
            produceFile = {
                context.dataStoreFile("splash_tutorial_settings.json")
            }
        )
    }
    @Singleton
    @Provides
    fun provideGetSplashTutorialImages(
        repository: SplashTutorialRepository
    ): GetSplashTutorialImages {
        return GetSplashTutorialImages(repository)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SplashTutorialBindModule {

    @Binds
    @Singleton
    abstract fun bindSplashTutorialRepository(
        defaultRepository: DefaultSplashTutorialRepository
    ): SplashTutorialRepository

    @Binds
    @Singleton
    abstract fun bindSplashTutorialRemoteDataSource(
        defaultRemoteDataSource: DefaultSplashTutorialRemoteDataSource
    ): SplashTutorialRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindSplashTutorialLocalDataSource(
        defaultLocalDataSource: DefaultSplashTutorialLocalDataSource
    ): SplashTutorialLocalDataSource

}