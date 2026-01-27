package com.prometheus_service.midas.shared.timber

import android.util.Log
import timber.log.Timber

class DefaultLoggingTree : Timber.DebugTree() {
    init {
        Log.d("DefaultLoggingTree", "Timber initialized with DefaultLoggingTree")
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
        Log.d("DefaultLoggingTree", "Log entry: $message, throwable: ${t?.message ?: "null"}")
    }
}