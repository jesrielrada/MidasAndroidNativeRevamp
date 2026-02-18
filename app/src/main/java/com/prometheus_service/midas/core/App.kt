package com.prometheus_service.midas.core

import android.app.Application
import android.webkit.WebView
import com.facebook.stetho.Stetho
import com.prometheus_service.midas.BuildConfig
import com.prometheus_service.midas.cmspushylib.PushModule
import com.prometheus_service.midas.shared.timber.DefaultLoggingTree
import dagger.hilt.android.HiltAndroidApp
import me.pushy.sdk.Pushy
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(DefaultLoggingTree())
        Stetho.initializeWithDefaults(this)
        WebView.setWebContentsDebuggingEnabled(true)

        PushModule.subscribeNoLogin(this)
        PushModule.startListening(this)
        val token = Pushy.getDeviceCredentials(this)?.token ?: "No token received. Please relaunch the app."
        Timber.d("Pushy token: $token")
    }
}