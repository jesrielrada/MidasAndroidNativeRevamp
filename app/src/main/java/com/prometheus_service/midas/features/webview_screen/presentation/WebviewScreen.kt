package com.prometheus_service.midas.features.webview_screen.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun WebviewScreen(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "This is webview screen",
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}