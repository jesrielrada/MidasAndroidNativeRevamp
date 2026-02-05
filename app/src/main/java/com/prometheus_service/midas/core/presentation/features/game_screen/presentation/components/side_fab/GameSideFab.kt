package com.prometheus_service.midas.core.presentation.features.game_screen.presentation.components.side_fab

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.prometheus_service.midas.FlavorConfig.DEFAULT_COLOR
import com.prometheus_service.midas.R
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.GameScreenUiState
import com.prometheus_service.midas.core.presentation.features.game_screen.presentation.GameSideFabState
import kotlinx.coroutines.launch
import timber.log.Timber

private const val topMargin = 50f
private const val bottomMargin = 50f

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GameSideFab(
    modifier: Modifier = Modifier,
    uiState: GameScreenUiState,
    onClickSideFab: () -> Unit,
    onClickHomeButton: () -> Unit,
    onClickReturnButton: () -> Unit,
    onClickDepositButton: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    val offsetY = uiState.sideFabOffsetY
    val height = uiState.sideFabHeight
    val parentHeight = uiState.parentHeight

    ConstraintLayout(
        modifier = modifier
            .padding(0.dp, 20.dp, 0.dp, 0.dp)
            .height(120.dp)
            .zIndex(10f)
            .pointerInput(parentHeight, height) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    val verticalDrag = (uiState.sideFabOffsetY.value + dragAmount.y)
                        .coerceIn(
                            topMargin,
                            uiState.parentHeight.toFloat() - uiState.sideFabHeight.toFloat() - bottomMargin
                        )

                    scope.launch {
                        offsetY.snapTo(verticalDrag)
                    }
                }
            }
    ) {

        val (sideFab, sideFabButtons) = createRefs()

        GlideImage(
            modifier = Modifier
                .width(30.dp)
                .height(50.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onClickSideFab()
                }
                .constrainAs(sideFab) {
                    centerVerticallyTo(parent)
                    end.linkTo(parent.end)
                },
            model = R.drawable.shape_halfcircle,
            contentDescription = null,
        )

        GlideImage(
            modifier = Modifier
                .size(22.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onClickSideFab()
                }
                .constrainAs(sideFabButtons) {
                    centerVerticallyTo(sideFab)
                    end.linkTo(sideFab.end)
                },
            model = R.drawable.vector_more,
            contentDescription = null
        )

        if (uiState.sideFabState == GameSideFabState.OPEN) {
            Box(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 20.dp, 0.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(DEFAULT_COLOR)
                    .constrainAs(createRef()) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
                    .clickable {
                        onClickHomeButton()
                    }
            ) {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(id = R.drawable.vector_home),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 40.dp, 0.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(DEFAULT_COLOR)
                    .constrainAs(createRef()) {
                        end.linkTo(parent.end)
                        centerVerticallyTo(parent)
                    }
                    .clickable {
                        onClickReturnButton()
                    }
            ) {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(id = R.drawable.vector_return),
                    contentDescription = null,
                    tint = Color.White
                )
            }
            Box(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 20.dp, 0.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(DEFAULT_COLOR)
                    .constrainAs(createRef()) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        onClickDepositButton()
                    }
            ) {
                Icon(
                    modifier = Modifier.padding(5.dp),
                    painter = painterResource(id = R.drawable.vector_funds),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}