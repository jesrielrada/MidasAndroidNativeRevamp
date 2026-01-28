package com.prometheus_service.midas.core.data.features.remote_domains.util.serializer

import androidx.datastore.core.Serializer
import com.prometheus_service.midas.core.domain.features.remote_domains.model.RemoteDomainsModel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object RemoteDomainsSerializer : Serializer<RemoteDomainsModel> {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    override val defaultValue: RemoteDomainsModel
        get() = RemoteDomainsModel()

    override suspend fun readFrom(input: InputStream): RemoteDomainsModel {
        return try {
            Timber.d("Reading RemoteDomainsModel...")
            json.decodeFromString(
                deserializer = RemoteDomainsModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e(e, "Failed to read RemoteDomainsModel: Serialization error")
            defaultValue
        } catch (e: Exception) {
            Timber.e(e, "Failed to read RemoteDomainsModel: IO error")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: RemoteDomainsModel,
        output: OutputStream
    ) {
        try {
            output.write(
                json.encodeToString(
                    serializer = RemoteDomainsModel.serializer(),
                    value = t
                ).encodeToByteArray()
            )
            Timber.d("Success in writing RemoteDomainsModel")
        } catch (e: Exception) {
            Timber.e(e, "Failed to write RemoteDomainsModel")
            throw e
        }
    }
}
