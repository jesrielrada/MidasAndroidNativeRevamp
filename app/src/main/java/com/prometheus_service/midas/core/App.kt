package com.prometheus_service.midas.core

import android.app.Application
import android.webkit.WebView
import com.facebook.stetho.Stetho
import com.prometheus_service.midas.BuildConfig
import com.prometheus_service.midas.shared.timber.DefaultLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(DefaultLoggingTree())
        Stetho.initializeWithDefaults(this)
        WebView.setWebContentsDebuggingEnabled(true)
    }
}