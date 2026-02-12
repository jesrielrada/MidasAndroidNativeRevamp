package com.prometheus_service.midas.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.prometheus_service.midas.core.data.features.second_stage.local.DefaultSecondStageDataSource
import com.prometheus_service.midas.core.data.features.second_stage.local.SecondStageDataSource
import com.prometheus_service.midas.core.data.features.second_stage.repository.DefaultSecondStageRepository
import com.prometheus_service.midas.core.data.features.second_stage.util.serializer.SecondStageSerializer
import com.prometheus_service.midas.core.data.shared.remote_config.DefaultRemoteConfigRepository
import com.prometheus_service.midas.core.data.shared.remote_config.local.DefaultRemoteConfigLocalDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.local.RemoteConfigLocalDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.remote.DefaultRemoteConfigRemoteDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.remote.RemoteConfigRemoteDataSource
import com.prometheus_service.midas.core.data.shared.remote_config.util.serializer.RemoteConfigSerializer
import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import com.prometheus_service.midas.core.domain.features.second_stage.repository.SecondStageRepository
import com.prometheus_service.midas.core.domain.shared.remote_config.RemoteConfigRepository
import com.prometheus_service.midas.core.domain.shared.remote_config.model.RemoteConfigModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecondStageModule {

    @Singleton
    @Provides
    fun provideRemoteConfigDataStore(
        @ApplicationContext context: Context
    ): DataStore<SecondStageModel> {
        return DataStoreFactory.create(
            serializer = SecondStageSerializer,
            produceFile = {
                context.dataStoreFile("second_stage_settings.json")
            }
        )
    }
}


@Module
@InstallIn(SingletonComponent::class)
abstract class SecondStageBindModule {
    @Binds
    @Singleton
    abstract fun bindSecondStageRepository(
        defaultRepository: DefaultSecondStageRepository
    ): SecondStageRepository


    @Binds
    @Singleton
    abstract fun bindSecondStageLocalDataSource(
        defaultDataSource: DefaultSecondStageDataSource
    ): SecondStageDataSource

}
