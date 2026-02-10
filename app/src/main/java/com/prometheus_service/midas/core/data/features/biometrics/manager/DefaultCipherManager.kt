package com.prometheus_service.midas.core.data.features.biometrics.manager

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProperties.BLOCK_MODE_CBC
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import com.prometheus_service.midas.core.domain.features.biometrics.manager.CipherManager
import com.prometheus_service.midas.core.domain.features.biometrics.model.CipherTextWrapper
import timber.log.Timber
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class DefaultCipherManager @Inject constructor() : CipherManager {

    companion object {
        const val KEY_SIZE = 256
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val CIPHER_TRANSFORMATION =
            "$KEY_ALGORITHM_AES/$BLOCK_MODE_CBC/$ENCRYPTION_PADDING_PKCS7"
    }

    private var _cipher: Cipher? = Cipher.getInstance(CIPHER_TRANSFORMATION)
    override val cipher: Cipher?
        get() = _cipher

    override fun getSecretKey(key: String): SecretKey {
        // If SecretKey was previously created for that keyName, then grab and return it.
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null) // Keystore must be loaded before it can be accessed

        try {
            keyStore.getKey(key, null)?.let { return it as SecretKey }
        } catch (e: Exception) {
            // key can no longer be used because it has been permanently invalidated
            // delete key from key store
            Timber.e("Failed to get secret key, $e")
            keyStore.deleteEntry(key)
        }

        // if you reach here, then a new SecretKey must be generated for that keyName
        val paramsBuilder = KeyGenParameterSpec.Builder(
            key,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )

        paramsBuilder.apply {
            setBlockModes(BLOCK_MODE_CBC)
            setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
            setKeySize(KEY_SIZE)
            setUserAuthenticationRequired(true)
        }

        val keyGenParams = paramsBuilder.build()
        val keyGenerator = KeyGenerator.getInstance(
            KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        keyGenerator.init(keyGenParams)
        return keyGenerator.generateKey()
    }

    override fun setCipherMode(mode: Int, key: String, vector: ByteArray?) {
        val secretKey = getSecretKey(key)

        try {
            if (vector == null) {
                cipher?.init(mode, secretKey)
            } else {
                cipher?.init(mode, secretKey, IvParameterSpec(vector))
            }
        } catch (e: KeyPermanentlyInvalidatedException) {

            Timber.d("Failed to init encrypt cipher, key exception $e, reinitializing ...")

            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)

            keyStore.load(null)
            keyStore.deleteEntry(key)

            try {
                cipher?.init(Cipher.ENCRYPT_MODE, secretKey)
            } catch (e: Exception) {
                Timber.e("Failed again to init encrypt cipher, $e")
                _cipher = null
            }

        } catch (e: Exception) {
            Timber.e("Failed to init decrypt cipher, $e")
            _cipher = null
        }
    }

    override fun decryptData(cipherText: ByteArray): String? {
        val plainText = cipher?.doFinal(cipherText)
        Timber.d("Decrypted data: $plainText")

        return if (plainText != null && cipher != null) {
            String(plainText, Charset.forName("UTF-8"))
        } else {
            null
        }
    }

    override fun encryptData(plainText: String): CipherTextWrapper? {
        val cipherText = cipher?.doFinal(plainText.toByteArray(Charset.forName("UTF-8")))
        Timber.d("Encrypted data: $cipherText")

        return if (cipherText != null && cipher != null) {
            CipherTextWrapper(cipherText, cipher!!.iv)
        } else {
            null
        }
    }

}