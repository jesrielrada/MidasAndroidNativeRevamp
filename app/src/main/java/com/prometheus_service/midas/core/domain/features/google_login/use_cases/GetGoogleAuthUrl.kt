package com.prometheus_service.midas.core.domain.features.google_login.use_cases

import androidx.core.net.toUri
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.decodeBase64
import timber.log.Timber
import javax.inject.Inject


class GetGoogleAuthUrl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider
) {
    companion object Companion {
        private val REDIRECT_URI_REGEX = """\[text=(.*?)]""".toRegex()
    }

    suspend operator fun invoke(
        clientId: String,
        url: String,
        response: GetCredentialResponse,
    ): Result<String> {
        return withContext(dispatcherProvider.io) {
            runCatching {
                val token = extractIdToken(response)
                    ?: throw IllegalStateException("Google ID Token not found in response")

                val uri = url.toUri()
                val state = uri.getQueryParameter("state")

                val rawRedirectUri = uri.getQueryParameter("redirect_uri")?.decodeBase64()?.utf8()
                    ?: throw IllegalArgumentException("Missing or invalid redirect_uri")

                val targetBaseUrl = REDIRECT_URI_REGEX.find(rawRedirectUri)?.groups?.get(1)?.value
                    ?: throw IllegalArgumentException("Could not parse target URL from redirect_uri")

                "$targetBaseUrl#state=$state&id_token=$token"
            }.onFailure {
                Timber.d("Error getting google auth url: $it")
            }

        }
    }

    private fun extractIdToken(response: GetCredentialResponse): String? {
        val credential = response.credential
        if (credential !is CustomCredential ||
            credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return null
        }

        return try {
            GoogleIdTokenCredential.createFrom(credential.data).idToken
        } catch (e: GoogleIdTokenParsingException) {
            null
        }
    }
}