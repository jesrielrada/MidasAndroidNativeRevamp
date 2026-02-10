package com.prometheus_service.midas.core.domain.features.biometrics.repository

import com.prometheus_service.midas.core.domain.features.biometrics.model.CipherTextWrapper
import com.prometheus_service.midas.core.domain.features.biometrics.model.CurrentAccount
import kotlinx.coroutines.flow.Flow

interface BiometricsRepository {

    suspend fun getCipherTextWrapper(key: String): Flow<CipherTextWrapper?>

    suspend fun persistCipherTextWrapper(cipherTextWrapper: CipherTextWrapper, memberCode: String)

    suspend fun deleteCipherTextWrapper(memberCode: String)

    suspend fun getUsernames(): Flow<List<String>?>

    suspend fun persistUsernames(usernames: List<String>)

    suspend fun persistUsername(username: String)

    suspend fun deleteAccountsList()

    suspend fun setBiometricsEnabled(enabled: Boolean)

    suspend fun isBiometricsEnabled(): Flow<Boolean>

    suspend fun setCmsboEnabled(enabled: Boolean)

    suspend fun isCmsboEnabled(): Flow<Boolean>

    suspend fun parseRemoteData(remoteData: String): CurrentAccount?

    suspend fun doesUserExists(username: String): Flow<Boolean>
}



