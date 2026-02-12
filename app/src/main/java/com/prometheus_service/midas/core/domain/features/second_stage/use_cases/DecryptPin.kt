package com.prometheus_service.midas.core.domain.features.second_stage.use_cases

import android.util.Base64
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class DecryptPin @Inject constructor(
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
        encryptedPin: String?,
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
                    init(Cipher.DECRYPT_MODE, keySpec, iv)
                }

                val decodedBytes = Base64.decode(encryptedPin, Base64.DEFAULT)
                val decryptedBytes = cipher.doFinal(decodedBytes)
                String(decryptedBytes, Charsets.UTF_8)

            } catch (e: Exception) {
                Timber.e("Failure decrypting pin, ${e.localizedMessage}")
                null
            }
        }
    }
}