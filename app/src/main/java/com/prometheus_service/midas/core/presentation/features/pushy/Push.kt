package com.prometheus_service.midas.core.presentation.features.pushy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.prometheus_service.midas.core.presentation.activity.MainActivity
import com.prometheus_service.midas.core.presentation.features.pushy.utils.convertUrlToRoute
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class Push : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, 0, 0)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }
        super.onCreate(savedInstanceState)

        val url = intent.dataString ?: intent.extras?.getString("url")

        Timber.d("onCreate called with url: $url")

        url?.let {
            Timber.d("Starting activity to redirect .. $it")
            val route = convertUrlToRoute(it)
            startActivityToRedirectUrl(route)
        }

    }

    private fun startActivityToRedirectUrl(route: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION)
            if (route != null) {
                data = route.toUri()
            }
        }
        Timber.d("Calling startActivity with url: $route")
        startActivity(intent)
        finish()
    }

}