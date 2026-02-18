package com.prometheus_service.midas.core

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.webkit.WebView
import android.widget.Toast
import com.facebook.stetho.Stetho
import com.prometheus_service.midas.BuildConfig
import com.prometheus_service.midas.cmspushylib.PushModule
import com.prometheus_service.midas.core.domain.providers.DispatcherProvider
import com.prometheus_service.midas.shared.timber.DefaultLoggingTree
import dagger.hilt.android.HiltAndroidApp
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.pushy.sdk.Pushy
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private val applicationScope by lazy {
        CoroutineScope(SupervisorJob() + dispatcherProvider.io)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DefaultLoggingTree())
            Stetho.initializeWithDefaults(this)
            WebView.setWebContentsDebuggingEnabled(true)
        }

        initializePushNotification()
        initializeSentry()
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel()
    }

    private suspend fun handleDebugTokenCopy() {
        val token = Pushy.getDeviceCredentials(this@App)?.token
            ?: "No token received."

        // 3. Context switch back to Main ONLY for UI tasks
        withContext(dispatcherProvider.main) {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("PushyToken", token))

            Toast.makeText(this@App, "Token copied to clipboard", Toast.LENGTH_SHORT).show()
            Timber.d("Pushy token: $token")
        }
    }

    private fun initializePushNotification() {
        applicationScope.launch {
            PushModule.subscribeNoLogin(this@App)
            PushModule.startListening(this@App)

            if (BuildConfig.DEBUG) {
                handleDebugTokenCopy()
            }
        }
    }

    private fun initializeSentry() {
        SentryAndroid.init(this@App) { options ->
            options.dsn = BuildConfig.SentryDSN
            options.environment = getSentryEnvironment()
            options.isAnrEnabled = true
            options.isEnableUserInteractionTracing = true
            options.isAttachScreenshot = true
            options.isAttachViewHierarchy = true
            options.isEnableAppStartProfiling = true
            options.logs.isEnabled = true

            @Suppress("KotlinConstantConditions")
            if (BuildConfig.BuildEnv == "P" && BuildConfig.DEBUG.not()) {
                options.tracesSampleRate = 0.2
                options.profilesSampleRate = 0.2
                Timber.i("Sentry initialized with production environment")
            } else {
                options.isDebug = true
                options.setDiagnosticLevel(SentryLevel.ERROR)
                options.tracesSampleRate = 1.0
                options.profilesSampleRate = 1.0
                Timber.d("Sentry initialized with non production / non release environment")
            }
        }
    }

    private fun getSentryEnvironment(): String {
        return when (BuildConfig.BUILD_TYPE) {
            "uat" -> "${BuildConfig.FLAVOR.uppercase()}-UAT"
            "preproduction" -> "${BuildConfig.FLAVOR.uppercase()}-PREPROD"
            else -> "${BuildConfig.FLAVOR.uppercase()}-PROD"
        }
    }

}