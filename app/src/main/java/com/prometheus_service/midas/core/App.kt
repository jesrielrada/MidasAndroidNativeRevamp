package com.prometheus_service.midas.core

import android.app.Application
import com.prometheus_service.midas.shared.timber.DefaultLoggingTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DefaultLoggingTree())
    }
}