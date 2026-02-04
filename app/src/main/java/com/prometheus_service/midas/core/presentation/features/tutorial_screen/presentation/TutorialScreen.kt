package com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.event.TutorialScreenEvent
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.TutorialIndicatorSelectedColor
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.TutorialIndicatorUnSelectedColor
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.TutorialNextButtonDefaultColor
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.TutorialNextButtonFinishColor
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.TutorialNextButtonOnContainerDefaultColor
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme.TutorialNextButtonOnContainerFinishColor
import com.prometheus_service.midas.core.presentation.main_screen.presentation.TutorialScreenTranslations
import com.prometheus_service.midas.shared.theme.MidasAndroidNativeRevampTheme
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun TutorialScreen(
    modifier: Modifier = Modifier,
    viewModel: TutorialScreenViewModel = hiltViewModel(),
    tutorialTranslations: TutorialScreenTranslations,
    onTutorialFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TutorialScreenContent(
        modifier = modifier,
        uiState = uiState,
        tutorialTranslations = tutorialTranslations,
        onTutorialFinished = {
            viewModel.onEvent(TutorialScreenEvent.OnTutorialFinished)
            onTutorialFinished()
        }
    )
}

@Composable
fun TutorialScreenContent(
    modifier: Modifier = Modifier,
    uiState: TutorialScreeUiState,
    tutorialTranslations: TutorialScreenTranslations,
    onTutorialFinished: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { uiState.images.size })
    val isLastPage by remember { derivedStateOf { pagerState.currentPage == pagerState.pageCount - 1 } }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
        ) {

            TutorialViewPager(
                modifier = Modifier
                    .zIndex(0f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 100.dp),
                images = uiState.images,
                pagerState = pagerState
            )

            TutorialButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
                    .zIndex(5f),
                buttonDefaultLabel = tutorialTranslations.buttonDefaultLabel,
                buttonEndLabel = tutorialTranslations.buttonEndLabel,
                isLastPage = isLastPage,
                onButtonClicked = {
                    if (isLastPage) {
                        onTutorialFinished()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            TutorialPageIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 30.dp)
                    .zIndex(5f),
                imageCount = uiState.images.size,
                selectedIndex = pagerState.currentPage
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TutorialViewPager(
    modifier: Modifier = Modifier,
    images: List<Any>,
    pagerState: PagerState
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .height(530.dp)
            .width(310.dp)
    )
    { page ->
        GlideImage(
            contentScale = ContentScale.Fit,
            model = images[page],
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun TutorialButton(
    buttonDefaultLabel: String,
    buttonEndLabel: String,
    isLastPage: Boolean,
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
) {

    val buttonBgColor =
        if (isLastPage) TutorialNextButtonFinishColor else TutorialNextButtonDefaultColor
    val buttonTextColor =
        if (isLastPage) TutorialNextButtonOnContainerFinishColor else TutorialNextButtonOnContainerDefaultColor
    val buttonLabel = if (isLastPage) buttonEndLabel else buttonDefaultLabel

    val animatedButtonBgColor by animateColorAsState(
        targetValue = buttonBgColor,
        label = "ButtonColorAnimation"
    )

    val animatedButtonTextColor by animateColorAsState(
        targetValue = buttonTextColor,
        label = "ButtonTextColorAnimation"
    )


    Box(
        modifier = modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(animatedButtonBgColor)
            .width(200.dp)
            .height(40.dp)
            .clickable {
                onButtonClicked()
            }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            color = animatedButtonTextColor,
            text = buttonLabel,
            fontSize = 13.sp,
        )
    }
}

@Composable
fun TutorialPageIndicator(
    modifier: Modifier = Modifier,
    imageCount: Int,
    selectedIndex: Int,
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .height(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items(imageCount) { index ->
            if (index == selectedIndex) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(TutorialIndicatorSelectedColor)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(TutorialIndicatorUnSelectedColor)
                )
            }

            if (index != imageCount - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 6.dp))
            }
        }
    }

}

@Composable
@Preview(showBackground = true)
fun TutorialScreenContentPreview() {
    MidasAndroidNativeRevampTheme {
        TutorialScreenContent(
            modifier = Modifier,
            uiState = TutorialScreeUiState(),
            tutorialTranslations = TutorialScreenTranslations(),
            onTutorialFinished = {}
        )
    }
}
