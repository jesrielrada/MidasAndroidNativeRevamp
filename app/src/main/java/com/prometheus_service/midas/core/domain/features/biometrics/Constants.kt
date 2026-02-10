package com.prometheus_service.midas.core.domain.features.biometrics

import android.security.keystore.KeyProperties.BLOCK_MODE_CBC
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES

class Constants {
    companion object {
        const val KEY_SIZE = 256
        const val CIPHER_TRANSFORMATION = "$KEY_ALGORITHM_AES/$BLOCK_MODE_CBC/$ENCRYPTION_PADDING_PKCS7"
    }
}