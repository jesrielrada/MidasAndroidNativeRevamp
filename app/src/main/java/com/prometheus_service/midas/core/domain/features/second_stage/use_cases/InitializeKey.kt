package com.prometheus_service.midas.core.domain.features.second_stage.use_cases

import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InitializeKey @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) {
    companion object {
        private const val CIPHER_KEY_LEN = 16 //128 bits
    }

    suspend operator fun invoke(key: String): String {
        return withContext(dispatcherProvider.io) {
            key.padEnd(CIPHER_KEY_LEN, '0').take(CIPHER_KEY_LEN)
        }
    }

}