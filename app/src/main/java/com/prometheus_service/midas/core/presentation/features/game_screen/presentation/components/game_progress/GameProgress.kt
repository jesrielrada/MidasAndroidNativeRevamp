package com.prometheus_service.midas.core.presentation.features.game_screen.presentation.components.game_progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.prometheus_service.midas.R

@Composable
fun GameProgress(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    if (isLoading) {
        ConstraintLayout(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .zIndex(15f)
        ) {
            val verticalGuideline = createGuidelineFromTop(.4f)
            val (image, progress) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.game_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .constrainAs(image) {
                        top.linkTo(verticalGuideline)
                        centerHorizontallyTo(parent)
                    }
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .size(35.dp)
                    .padding(0.dp, 13.dp, 0.dp, 0.dp)
                    .constrainAs(progress) {
                        top.linkTo(image.bottom)
                        centerHorizontallyTo(parent)
                    },
                color = Color.White
            )
        }
    }
}