package com.prometheus_service.midas.core.domain.features.biometrics.model

import kotlinx.serialization.Serializable

@Serializable
data class BiometricsModel(
    val cipherList: List<Pair<String, CipherTextWrapper>>? = null,
    val isBiometricsEnabled: Boolean? = false,
    val isCmsboEnabled: Boolean? = false,
    val usernames: List<String>? = emptyList()
)

@Serializable
data class CurrentAccount(
    val memberCode: String? = null,
    val password: String? = null
)

@Serializable
data class CipherTextWrapper(
    val ciphertext: ByteArray,
    val initializationVector: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CipherTextWrapper

        if (!ciphertext.contentEquals(other.ciphertext)) return false
        if (!initializationVector.contentEquals(other.initializationVector)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ciphertext.contentHashCode()
        result = 31 * result + initializationVector.contentHashCode()
        return result
    }
}