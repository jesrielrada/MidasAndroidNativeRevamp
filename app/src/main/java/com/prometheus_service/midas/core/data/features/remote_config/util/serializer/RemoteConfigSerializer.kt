package com.prometheus_service.midas.core.data.features.remote_config.util.serializer

import androidx.datastore.core.Serializer
import com.prometheus_service.midas.core.domain.features.remote_config.model.RemoteConfigModel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object RemoteConfigSerializer : Serializer<RemoteConfigModel> {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: RemoteConfigModel
        get() = RemoteConfigModel()


    override suspend fun readFrom(input: InputStream): RemoteConfigModel {
        return try {
            Timber.d("Reading RemoteConfigModel...")
            json.decodeFromString(
                deserializer = RemoteConfigModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e(e, "Failed to read RemoteConfigModel: Serialization error")
            defaultValue
        } catch (e: Exception) {
            Timber.e(e, "Failed to read RemoteConfigModel: IO error")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: RemoteConfigModel,
        output: OutputStream
    ) {
        try {
            output.write(
                json.encodeToString(
                    serializer = RemoteConfigModel.serializer(),
                    value = t
                ).encodeToByteArray()
            )
            Timber.d("Success in writing RemoteConfigModel")
        } catch (e: Exception) {
            Timber.e(e, "Failed to write RemoteConfigModel")
            throw e
        }
    }
}