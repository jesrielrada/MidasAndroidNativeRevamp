package com.prometheus_service.midas.core.data.features.splash_tutorial.utils.serializer

import androidx.datastore.core.Serializer
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object SplashTutorialSerializer : Serializer<SplashTutorialModel> {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: SplashTutorialModel
        get() = SplashTutorialModel()


    override suspend fun readFrom(input: InputStream): SplashTutorialModel {
        return try {
            json.decodeFromString(
                deserializer = SplashTutorialModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e(e, "Failed to read SplashTutorialModel: Serialization error")
            defaultValue
        } catch (e: Exception) {
            Timber.e(e, "Failed to read SplashTutorialModel: IO error")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: SplashTutorialModel,
        output: OutputStream
    ) {
        try {
            output.write(
                json.encodeToString(
                    serializer = SplashTutorialModel.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to write SplashTutorialModel")
            throw e
        }
    }
}