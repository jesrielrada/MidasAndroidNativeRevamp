package com.prometheus_service.midas.core.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.fragment.app.FragmentActivity
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.DarkExtendedColors
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.LightExtendedColors
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.LocalExtendedColors
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent
import com.prometheus_service.midas.core.presentation.main_screen.presentation.MainScreen
import com.prometheus_service.midas.core.presentation.main_screen.presentation.MainScreenViewModel
import com.prometheus_service.midas.shared.theme.MidasAndroidNativeRevampTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme = isSystemInDarkTheme()
            val extendedColors = if (isDarkTheme) DarkExtendedColors else LightExtendedColors

            CompositionLocalProvider(
                LocalExtendedColors provides extendedColors
            ) {
                MidasAndroidNativeRevampTheme {
                    MainScreen(
                        viewModel = viewModel,
                        activity = this,
                        extendedColors = extendedColors
                    )
                }
            }

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onEvent(MainScreenEvent.CacheSessionCookies)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Timber.d("onNewIntent called with intent: $intent")
        intent.dataString?.let { url ->
            // You can now call functions directly on the VM from the Activity
            Timber.d("onNewIntent called with intent: $url")
            viewModel.onEvent(MainScreenEvent.HandlePushNotificationUrl(url))
        }
    }
}
