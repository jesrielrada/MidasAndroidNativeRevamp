@file:OptIn(ExperimentalGlideComposeApi::class)

package com.prometheus_service.midas.core.presentation.features.second_stage.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.prometheus_service.midas.R
import com.prometheus_service.midas.SecondStagePrimaryColor

@Composable
fun SecondStageScreen(
    modifier: Modifier = Modifier,
    viewModel: SecondStageScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val source = remember { MutableInteractionSource() }

    SecondStageScreenContent(
        uiState = uiState,
        source = source,
        modifier = modifier,
        onEnteredValue = {

        },
        onCompleteText = {

        },
        onCancel = {

        },
        onDelete = {

        }
    )
}


@Composable
fun SecondStageScreenContent(
    modifier: Modifier = Modifier,
    uiState: SecondStageScreenUiState,
    source: MutableInteractionSource,
    onEnteredValue: (String) -> Unit,
    onCompleteText: (String) -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0XFF0E0E0E))
    ) {

        val logoGuideline = createGuidelineFromTop(.1f)
        val (logoRef, pinContainerRef, cancelRef) = createRefs()

        Image(
            modifier = Modifier
                .size(80.dp)
                .constrainAs(logoRef) {
                    top.linkTo(logoGuideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(pinContainerRef.top)
                },
            painter = painterResource(id = R.drawable.brand_logo),
            contentDescription = null
        )

        PinFieldView(
            modifier = Modifier
                .padding(top = 10.dp)
                .constrainAs(createRef()) {
                    centerHorizontallyTo(parent)
                    top.linkTo(logoRef.bottom)
                },
            value = uiState.pinEnteredTexts,
            headerValue = uiState.pinHeaderValue
        )

        Column(modifier = Modifier.constrainAs(pinContainerRef) {
            top.linkTo(logoRef.bottom)
            centerHorizontallyTo(parent)
            bottom.linkTo(parent.bottom)
        }) {
            PinNumbers(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                source = source,
                onClick = {
                    if (uiState.pinEnteredTexts.length < 4) {
                        val newValue = uiState.pinEnteredTexts + it

                        onEnteredValue(newValue)


                        if (newValue.length == 4) {
                            onCompleteText(newValue)
                        }
                    }

                }, onClickDelete = {
                    onDelete()
                }
            )
        }

        CancelAttemptText(
            modifier = Modifier
                .padding(top = 30.dp)
                .constrainAs(cancelRef) {
                    top.linkTo(pinContainerRef.bottom)
                    centerHorizontallyTo(parent)
                }
                .clickable(
                    indication = null,
                    interactionSource = source
                ) {
                    onCancel()
                }, footerValue = uiState.pinFooterValue
        )
    }
}


@Composable
fun PinFieldView(
    modifier: Modifier = Modifier,
    value: String,
    headerValue: String = "Enter the lock screen password"
) {
    Column(modifier = modifier) {
        CustomCirclePinField(
            modifier = Modifier
                .height(48.dp)
                .width(147.dp)
                .align(Alignment.CenterHorizontally),
            value = value
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Enter the lock screen password",
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun CustomCirclePinField(
    modifier: Modifier = Modifier,
    value: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CustomCirclePinView(value = if (value.isNotEmpty()) value[0].toString() else "")
        Spacer(modifier = Modifier.size(22.dp))
        CustomCirclePinView(value = if (value.length > 1) value[1].toString() else "")
        Spacer(modifier = Modifier.size(22.dp))
        CustomCirclePinView(value = if (value.length > 2) value[2].toString() else "")
        Spacer(modifier = Modifier.size(22.dp))
        CustomCirclePinView(value = if (value.length > 3) value[3].toString() else "")
    }
}

@Composable
fun CustomCirclePinView(
    modifier: Modifier = Modifier,
    value: String
) {
    val fieldColor = if (value.isBlank()) Color(0XFF404954) else Color.White

    TextField(
        modifier = modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(fieldColor),
        value = value,
        singleLine = true,
        maxLines = 1,
        onValueChange = {

        }
    )
}

@Composable
fun CancelAttemptText(modifier: Modifier = Modifier, footerValue: String) {
    Text(
        modifier = modifier,
        text = "Cancel",
        color = SecondStagePrimaryColor,
        fontSize = 16.sp
    )
}

@Composable
fun PinNumberView(
    modifier: Modifier = Modifier,
    source: MutableInteractionSource,
    digit: String,
    onClick: (String) -> Unit,
) {
    Box(
        modifier = modifier.clickable(
            interactionSource = source,
            indication = null
        ) {
            onClick(digit)
        }) {
        GlideImage(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .size(75.dp),
            model = R.drawable.vector_circle_border,
            contentDescription = null
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = digit,
            fontSize = 36.sp,
            color = SecondStagePrimaryColor
        )
    }
}


@Composable
fun PinNumbers(
    modifier: Modifier = Modifier,
    source: MutableInteractionSource,
    onClick: (String) -> Unit,
    onClickDelete: () -> Unit
) {
    Column(modifier = modifier) {
        val row1 = listOf("1", "2", "3")
        val row2 = listOf("4", "5", "6")
        val row3 = listOf("7", "8", "9")

        PinNumberRows(
            values = row1,
            source = source,
            onClick = {
                onClick(it)
            }
        )
        PinNumberRows(
            values = row2,
            source = source,
            onClick = {
                onClick(it)
            }
        )
        PinNumberRows(
            values = row3,
            source = source,
            onClick = {
                onClick(it)
            }
        )
        PinNumberLastRow(
            source = source,
            onClick = {
                onClick(it)
            }, onClickDelete = {
                onClickDelete()
            }
        )
    }
}

@Composable
fun PinNumberRows(
    modifier: Modifier = Modifier,
    source: MutableInteractionSource,
    values: List<String>,
    onClick: (String) -> Unit,
) {
    Row(modifier = modifier.padding(top = 30.dp)) {
        values.forEach { value ->
            PinNumberView(
                digit = value,
                source = source,
                onClick = {
                    onClick(it)
                }
            )
        }
    }
}

@Composable
fun PinNumberLastRow(
    modifier: Modifier = Modifier,
    source: MutableInteractionSource,
    onClick: (String) -> Unit,
    onClickDelete: () -> Unit
) {
    Row(modifier = modifier.padding(top = 30.dp)) {
        Spacer(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .size(75.dp)
        )
        PinNumberView(
            digit = "0",
            source = source,
            onClick = {
                onClick(it)
            }
        )
        PinDeleteView(
            source = source,
            onClickDelete = onClickDelete
        )
    }
}

@Composable
fun PinDeleteView(
    modifier: Modifier = Modifier,
    onClickDelete: () -> Unit,
    source: MutableInteractionSource
) {
    Box(
        modifier = modifier
            .clickable(
                interactionSource = source,
                indication = null
            ) {
                onClickDelete()
            },
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .size(75.dp),
            model = R.drawable.vector_circle_border,
            contentDescription = null
        )

        GlideImage(
            modifier = Modifier.size(30.dp),
            model = R.drawable.vector_backspace,
            contentDescription = null
        )
    }
}
