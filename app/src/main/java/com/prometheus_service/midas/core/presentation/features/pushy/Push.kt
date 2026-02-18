package com.prometheus_service.midas.core.presentation.features.pushy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.prometheus_service.midas.core.presentation.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Push : AppCompatActivity() {
    private fun startActivityToRedirectUrl(url: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_NO_ANIMATION)
            if (url != null) {
                data = url.toUri()
            }
        }
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, 0, 0)
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(0, 0)
        }

        super.onCreate(savedInstanceState)

        val url = intent.dataString ?: intent.extras?.getString("url")
        startActivityToRedirectUrl(url)
    }

}