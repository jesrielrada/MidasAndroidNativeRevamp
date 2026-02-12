package com.prometheus_service.midas.core.domain.features.second_stage.use_cases

import android.util.Base64
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class EncryptPin @Inject constructor(
    private val initializeKey: InitializeKey,
    private val initializeVector: InitializeVector,
    private val dispatcherProvider: DispatcherProvider
) {
    companion object {
        private const val TRANSFORMATION = "AES/CBC/PKCS5PADDING"
        private const val ALGORITHM = "AES"
    }

    suspend operator fun invoke(
        key: String,
        value: String?,
        clientSecret: String
    ): String? {
        return withContext(dispatcherProvider.io) {
            try {
                val keyString = initializeKey.invoke(key)
                val keyBytes = initializeVector.invoke(clientSecret)
                    .toByteArray(charset("UTF-8"))
                val specBytes = keyString.toByteArray(charset("UTF-8"))

                val iv = IvParameterSpec(keyBytes)
                val keySpec = SecretKeySpec(specBytes, ALGORITHM)

                val cipher = Cipher.getInstance(TRANSFORMATION).apply {
                    init(Cipher.ENCRYPT_MODE, keySpec, iv)
                }

                val encryptedBytes = cipher.doFinal(value?.toByteArray())
                Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
            } catch (e: Exception) {
                Timber.e("Failure encrypting pin, ${e.localizedMessage}")
                null
            }
        }
    }
}
