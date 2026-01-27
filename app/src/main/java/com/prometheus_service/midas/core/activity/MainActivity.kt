package com.prometheus_service.midas.core.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.prometheus_service.midas.core.main_screen.presentation.MainScreen
import com.prometheus_service.midas.shared.theme.MidasAndroidNativeRevampTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MidasAndroidNativeRevampTheme {
                MainScreen()
            }
        }
    }
}
