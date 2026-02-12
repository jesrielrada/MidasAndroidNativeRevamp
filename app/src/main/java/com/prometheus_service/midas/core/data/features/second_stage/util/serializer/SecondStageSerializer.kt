package com.prometheus_service.midas.core.data.features.second_stage.util.serializer

import androidx.datastore.core.Serializer
import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object SecondStageSerializer : Serializer<SecondStageModel> {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: SecondStageModel
        get() = SecondStageModel()

    override suspend fun readFrom(input: InputStream): SecondStageModel {
        return try {
            json.decodeFromString(
                deserializer = SecondStageModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e(e, "Failed to read SecondStageModel: Serialization error")
            defaultValue
        } catch (e: Exception) {
            Timber.e(e, "Failed to read SecondStageModel: IO error")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: SecondStageModel,
        output: OutputStream
    ) {
        try {
            output.write(
                json.encodeToString(
                    serializer = SecondStageModel.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to write SecondStageModel")
            throw e
        }
    }
}