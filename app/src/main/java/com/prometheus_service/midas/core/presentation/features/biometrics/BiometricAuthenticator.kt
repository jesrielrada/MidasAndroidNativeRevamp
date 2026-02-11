package com.prometheus_service.midas.core.presentation.features.biometrics

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticator (private val activity: FragmentActivity){
    fun authenticate(
        cryptoObject: BiometricPrompt.CryptoObject,
        promptInfo: BiometricPrompt.PromptInfo,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val prompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess(result)
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError(errorCode, errString)
            }
        })

        prompt.authenticate(promptInfo, cryptoObject)
    }
}