package com.prometheus_service.midas.core.presentation.features.pushy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.prometheus_service.midas.R
import com.prometheus_service.midas.cmspushylib.data.models.PushNotif
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.O)
class PushyHelper(private val context: Context) {

    companion object {
        const val PRIMARY_CHANNEL_ID = "default_channel"
        const val PRIMARY_CHANNEL_NAME = "Default Notifications"
    }

    private var notificationManager =
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            PRIMARY_CHANNEL_ID,
            PRIMARY_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Standard app notifications"
        }
        notificationManager.createNotificationChannel(channel)
    }

    suspend fun buildNotification(push: PushNotif): NotificationCompat.Builder {
        return withContext(Dispatchers.IO) {
            Timber.d("Building notification for: ${push.defaultTitle}")

            val pushMessage = processMessage(push.defaultMessage)
            val imageUrl = extractImageUrl(push.defaultMessage)
            val builder = NotificationCompat.Builder(context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.notif_small)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher_round
                    )
                )
                .setContentTitle(push.defaultTitle)
                .setContentText(pushMessage)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

            imageUrl?.let { url ->
                try {
                    val bitmap = withContext(Dispatchers.IO) {
                        Glide.with(context)
                            .asBitmap()
                            .load(url)
                            .submit()
                            .get() // Still a get, but wrapped in IO dispatcher
                    }
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                } catch (e: Exception) {
                    Timber.e(e, "Failed to load notification image")
                    builder.setStyle(NotificationCompat.BigTextStyle().bigText(pushMessage))
                }
            }

            // Setup Intent
            val intent = Intent(context, Push::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                this.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                push.deepLink?.let { this.data = it.toUri() }
            }

            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            builder.setContentIntent(pendingIntent)
        }

    }

    suspend fun displayNotification(id: Int, builder: NotificationCompat.Builder) {
        withContext(Dispatchers.Main) {
            // Check for Android 13+ permission before notifying
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    Timber.w("Missing POST_NOTIFICATIONS permission")
                    return@withContext
                }
            }

            notificationManager.notify(id, builder.build())
        }
    }

    private fun processMessage(rawMessage: String?): String {
        if (rawMessage.isNullOrEmpty()) return ""
        return "(?:https?|ftp)://\\S+".toRegex().replace(rawMessage, "").trim()
    }

    private fun extractImageUrl(rawMessage: String?): String? {
        if (rawMessage.isNullOrEmpty()) return null
        return "(?:https?|ftp)://\\S+(jpg|jpeg|tiff|png|bmp)".toRegex().find(rawMessage)?.value
    }
}