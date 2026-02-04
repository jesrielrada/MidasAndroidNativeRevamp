package com.prometheus_service.midas.core.data.shared.multi_language.util.serializer

import androidx.datastore.core.Serializer
import com.prometheus_service.midas.core.domain.shared.multi_language.model.MultiLanguageModel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object MultiLanguageSerializer : Serializer<MultiLanguageModel> {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: MultiLanguageModel
        get() = MultiLanguageModel()

    override suspend fun readFrom(input: InputStream): MultiLanguageModel {
        return try {
            json.decodeFromString(
                deserializer = MultiLanguageModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e(e, "Failed to read MultiLanguageModel: Serialization error")
            defaultValue
        } catch (e: Exception) {
            Timber.e(e, "Failed to read MultiLanguageModel: IO error")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: MultiLanguageModel,
        output: OutputStream
    ) {
        try {
            output.write(
                json.encodeToString(
                    serializer = MultiLanguageModel.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to write MultiLanguageModel")
            throw e
        }
    }
}
