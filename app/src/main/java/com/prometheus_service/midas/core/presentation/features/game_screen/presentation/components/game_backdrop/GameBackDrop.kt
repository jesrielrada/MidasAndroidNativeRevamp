package com.prometheus_service.midas.core.presentation.features.game_screen.presentation.components.game_backdrop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun GameBackdrop(
    modifier: Modifier = Modifier,
    shouldDisplayBackdrop: Boolean = false,
    onClickBackdrop: () -> Unit
) {

    if (shouldDisplayBackdrop) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = .9f))
                .clickable {
                    onClickBackdrop()
                }
        )
    }
}