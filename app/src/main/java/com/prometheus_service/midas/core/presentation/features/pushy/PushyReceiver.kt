package com.prometheus_service.midas.core.presentation.features.pushy

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.prometheus_service.midas.cmspushylib.data.models.PushNotif
import com.prometheus_service.midas.cmspushylib.receivers.PushReceiver
import com.prometheus_service.midas.core.presentation.features.pushy.utils.getDeviceLanguage
import com.prometheus_service.midas.core.presentation.features.pushy.utils.getPushyLanguage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class PushyReceiver : PushReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun notify(
        context: Context,
        pushNotif: PushNotif,
        pushId: String
    ) {
        val deviceLang: String = getDeviceLanguage(context).language
        val pushyLang = getPushyLanguage(pushNotif.language)

        Timber.d("Device language: $deviceLang, Pushy language: $pushyLang")

        if (isLanguageNotEmpty(deviceLang, pushyLang)
            && isLanguageEqual(deviceLang, pushyLang)
        ) {

            val randomId = System.currentTimeMillis().toInt()
            val pushyHelper = PushyHelper(context)

            MainScope().launch {
                try {
                    Timber.d("Push notification received: $pushNotif")
                    // Call the suspend function
                    val pushyBuilder = pushyHelper.buildNotification(pushNotif)
                    // Back to Main thread to display (NotificationManager is thread safe, but good practice)
                    pushyHelper.displayNotification(randomId, pushyBuilder)
                } catch (e: Exception) {
                    Timber.e(e, "Failed to display notification")
                }
            }
        }

    }

    private fun isLanguageNotEmpty(deviceLang: String, countryLang: String): Boolean {
        return deviceLang.isNotEmpty() && countryLang.isNotEmpty()
    }

    private fun isLanguageEqual(deviceLang: String, countryLang: String): Boolean {
        return (deviceLang.contains("en") && countryLang.contains("en") ||
                deviceLang.contains("km") && countryLang.contains("km") ||
                deviceLang.contains("zh") && countryLang.contains("zh") ||
                deviceLang.contains("id") && countryLang.contains("id") ||
                deviceLang.contains("ja") && countryLang.contains("ja") ||
                deviceLang.contains("ko") && countryLang.contains("ko") ||
                deviceLang.contains("th") && countryLang.contains("th") ||
                deviceLang.contains("vi") && countryLang.contains("vi") ||
                deviceLang.contains("in") && countryLang.contains("in")
                )
    }
}