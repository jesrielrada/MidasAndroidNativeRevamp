package com.prometheus_service.midas.core.data.shared.app_config.util.serializer

import androidx.datastore.core.Serializer
import com.prometheus_service.midas.core.domain.features.splash_tutorial.model.SplashTutorialModel
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object AppConfigSerializer : Serializer<AppConfigModel> {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    override val defaultValue: AppConfigModel
        get() = AppConfigModel()

    override suspend fun readFrom(input: InputStream): AppConfigModel {
        return try {
            Timber.d("Reading AppConfigModel...")
            json.decodeFromString(
                deserializer = AppConfigModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            Timber.e(e, "Failed to read AppConfigModel: Serialization error")
            defaultValue
        } catch (e: Exception) {
            Timber.e(e, "Failed to read AppConfigModel: IO error")
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: AppConfigModel,
        output: OutputStream
    ) {
        try {
            output.write(
                json.encodeToString(
                    serializer = AppConfigModel.serializer(),
                    value = t
                ).encodeToByteArray()
            )
            Timber.d("Success in writing AppConfigModel")
        } catch (e: Exception) {
            Timber.e(e, "Failed to write AppConfigModel")
            throw e
        }
    }
}
