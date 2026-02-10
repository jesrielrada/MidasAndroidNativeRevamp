package com.prometheus_service.midas.core.domain.features.biometrics.manager

import com.prometheus_service.midas.core.domain.features.biometrics.model.CipherTextWrapper
import javax.crypto.Cipher
import javax.crypto.SecretKey

interface CipherManager {

    val cipher: Cipher?
    fun getSecretKey(key: String): SecretKey
    fun setCipherMode(mode: Int, key: String, vector: ByteArray?)
    fun decryptData(cipherText: ByteArray): String?
    fun encryptData(plainText: String): CipherTextWrapper?
}