package com.prometheus_service.midas.core.data.features.biometrics.repository

import com.prometheus_service.midas.core.data.features.biometrics.data_source.BiometricsLocalDataSource
import com.prometheus_service.midas.core.domain.features.biometrics.model.BiometricsModel
import com.prometheus_service.midas.core.domain.features.biometrics.repository.BiometricsRepository
import com.prometheus_service.midas.core.domain.features.biometrics.model.CipherTextWrapper
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.orEmpty

class DefaultBiometricsRepository @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val localDataSource: BiometricsLocalDataSource,
    private val json: Json
) : BiometricsRepository {

    override suspend fun getCipherTextWrapper(username: String): Flow<CipherTextWrapper?> {
        return localDataSource.biometricsModel.map { model ->
            model.cipherList?.find { it.first == username }?.second
        }.distinctUntilChanged().flowOn(dispatcherProvider.io)
    }

    override suspend fun persistCipherTextWrapper(
        cipherTextWrapper: CipherTextWrapper,
        memberCode: String
    ) {
        withContext(dispatcherProvider.io) {
            val currentModel = localDataSource.biometricsModel.first()
            val updatedList = currentModel.cipherList.orEmpty().toMutableList().apply {
                removeAll { it.first == memberCode }
                add(memberCode to cipherTextWrapper)
            }
            localDataSource.cacheBiometricsModel(
                currentModel.copy(cipherList = updatedList)
            )
        }
    }

    override suspend fun deleteCipherTextWrapper(memberCode: String) {
        withContext(dispatcherProvider.io) {
            val currentModel = localDataSource.biometricsModel.first()
            val updatedList = currentModel.cipherList.orEmpty().toMutableList().apply {
                removeAll { it.first == memberCode }
            }
            localDataSource.cacheBiometricsModel(
                currentModel.copy(cipherList = updatedList)
            )
        }
    }

    override suspend fun getUsernames(): Flow<List<String>?> {
        return withContext(dispatcherProvider.io) {
            localDataSource.biometricsModel.map { model ->
                model.usernames
            }.distinctUntilChanged()
        }
    }

    override suspend fun persistUsernames(usernames: List<String>) {
        withContext(dispatcherProvider.io) {
            val currentModel = localDataSource.biometricsModel.first()
            val updatedUsernames = currentModel.usernames.orEmpty().toMutableList().apply {
                addAll(usernames)
            }.distinct()
            localDataSource.cacheBiometricsModel(
                currentModel.copy(usernames = updatedUsernames)
            )
        }
    }

    override suspend fun persistUsername(username: String) {
        withContext(dispatcherProvider.io) {
            val currentModel = localDataSource.biometricsModel.first()
            val updatedUsernames = currentModel.usernames.orEmpty().toMutableList().apply {
                add(username)
            }.distinct()
            localDataSource.cacheBiometricsModel(
                currentModel.copy(usernames = updatedUsernames)
            )
        }
    }

    override suspend fun deleteAccountsList() {
        withContext(dispatcherProvider.io) {
            localDataSource.cacheBiometricsModel(
                BiometricsModel(usernames = emptyList())
            )
        }
    }

    override suspend fun deleteAccount(username: String) {
        withContext(dispatcherProvider.io) {
            val currentModel = localDataSource.biometricsModel.first()

            val updatedUsernames = currentModel.usernames.orEmpty().toMutableList().apply {
                remove(username)
            }.distinct()

            val cipherList = currentModel.cipherList.orEmpty().toMutableList().apply {
                removeAll { it.first == username }
            }.distinct()

            localDataSource.cacheBiometricsModel(
                currentModel.copy(
                    usernames = updatedUsernames,
                    cipherList = cipherList
                )
            )
        }
    }

    override suspend fun setBiometricsEnabled(enabled: Boolean, cmsboEnabled: Boolean) {
        withContext(dispatcherProvider.io) {
            val currentModel = localDataSource.biometricsModel.first()
            localDataSource.cacheBiometricsModel(
                currentModel.copy(
                    isUserEnabled = enabled,
                    isCmsboEnabled = cmsboEnabled
                )
            )
        }
    }

    override suspend fun setBiometricsCmsboEnabled(enabled: Boolean) {
        withContext(dispatcherProvider.io) {
            val currentModel = localDataSource.biometricsModel.first()
            localDataSource.cacheBiometricsModel(
                currentModel.copy(
                    isCmsboEnabled = enabled
                )
            )
        }
    }

    override suspend fun isBiometricsEnabled(): Flow<Boolean> {
        return localDataSource.biometricsModel.map { model ->
            model.isUserEnabled ?: false
        }.distinctUntilChanged()
    }

    override suspend fun isCmsboEnabled(): Flow<Boolean> {
        return localDataSource.biometricsModel.map { model ->
            model.isCmsboEnabled ?: false
        }.distinctUntilChanged()
    }

    override suspend fun parseRemoteData(remoteData: String): CurrentAccount? {
        return withContext(dispatcherProvider.io) {
            runCatching {
                json.decodeFromString<CurrentAccount>(remoteData)
            }.getOrElse {
                Timber.e("Failed to parse remote data: $it")
                null
            }
        }
    }

    override suspend fun doesUserExists(username: String): Flow<Boolean> {
        return withContext(dispatcherProvider.io) {
            localDataSource.biometricsModel.map { model ->
                model.usernames?.any { it == username } == true
            }
        }
    }
}
