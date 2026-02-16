package com.prometheus_service.midas.core.data.providers.util

import androidx.datastore.core.Serializer
import com.prometheus_service.midas.core.domain.features.biometrics.model.BiometricsModel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object BiometricsSerializer : Serializer<BiometricsModel> {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    override val defaultValue: BiometricsModel
        get() = BiometricsModel()

    override suspend fun readFrom(input: InputStream): BiometricsModel {
        return try {
            json.decodeFromString(
                deserializer = BiometricsModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e(e, "Failed to read BiometricsModel: Serialization error")
            defaultValue
        } catch (e: Exception) {
            Timber.e(e, "Failed to read BiometricsModel: IO error")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: BiometricsModel,
        output: OutputStream
    ) {
        try {
            output.write(
                json.encodeToString(
                    serializer = BiometricsModel.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to write BiometricsModel")
            throw e
        }
    }
}
