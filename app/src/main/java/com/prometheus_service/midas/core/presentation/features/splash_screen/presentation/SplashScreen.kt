package com.prometheus_service.midas.core.presentation.features.splash_screen.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.ContextThemeWrapper
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.github.ybq.android.spinkit.R
import com.github.ybq.android.spinkit.SpinKitView
import com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.event.SplashScreenEvent
import com.prometheus_service.midas.shared.theme.MidasAndroidNativeRevampTheme
import kotlinx.coroutines.delay
import timber.log.Timber

private const val SCROLL_INTERVAL = 2000L // 2 seconds

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashScreenViewModel = hiltViewModel(),
    onScrollFinished: () -> Unit,
    onClickSkipBtn: () -> Unit,
    isReadyToHide: Boolean = false
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(isReadyToHide) {
        if (isReadyToHide) {
            Timber.d("Condition met, displaying skip button")
            viewModel.onEvent(SplashScreenEvent.DisplaySkipButton)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            Timber.d("Disposing Splash Screen, stopping splash progress")
            viewModel.onEvent(SplashScreenEvent.StopSplashProgress)
        }
    }

    SplashScreenContent(
        modifier = modifier,
        uiState = uiState,
        onClickSkipBtn = onClickSkipBtn,
        onScrollFinished = onScrollFinished
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    uiState: SplashScreenUiState,
    onClickSkipBtn: () -> Unit,
    onScrollFinished: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        ConstraintLayout(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            val (versionRef, progressRef, skipBtnRef, brandLogoRef) = createRefs()
            val versionVerticalGuideline = createGuidelineFromTop(.39f)
            val progressVerticalGuideline = createGuidelineFromTop(.57f)


            SplashViewPager(
                images = uiState.images,
                onScrollFinished = onScrollFinished,
                scrollInterval = SCROLL_INTERVAL
            )

            SplashAppVersion(
                modifier = Modifier.constrainAs(versionRef) {
                    top.linkTo(versionVerticalGuideline)
                    centerHorizontallyTo(parent)
                },
                version = uiState.appVersion
            )

            SplashScreenBrandLogo(
                modifier = Modifier.constrainAs(brandLogoRef) {
                    centerTo(parent)
                },
                contentDescription = null,
                brandLogo = uiState.brandLogo
            )

            if (uiState.isSkipVisible) {
                SplashSkipButton(
                    onClick = onClickSkipBtn,
                    modifier = Modifier.constrainAs(skipBtnRef) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    },
                    skipButtonLabel = uiState.skipLabel
                )
            }

            if (uiState.isProgressVisible) {
                SplashScreenCustomProgressView(
                    modifier = Modifier.constrainAs(progressRef) {
                        top.linkTo(progressVerticalGuideline)
                        centerHorizontallyTo(parent)
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreenBrandLogo(
    modifier: Modifier = Modifier,
    contentDescription: String?,
    brandLogo: Int
) {
    Image(
        painter = painterResource(brandLogo),
        contentDescription = contentDescription,
        modifier = modifier
            .width(250.dp)
            .height(76.dp)
    )
}

@Composable
fun SplashScreenCustomProgressView(
    modifier: Modifier = Modifier
){
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 540f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300)
        )
    )

    Box(
        modifier = modifier
            .size(120.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black.copy(.6f)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = com.prometheus_service.midas.R.drawable.splash_hourglass),
            contentDescription = null,
            modifier = Modifier
                .size(65.dp)
                .align(Alignment.Center)
                .zIndex(10f)
                .graphicsLayer {
                    rotationZ = angle
                }
        )
    }
}
@Composable
fun SplashScreenProgressView(
    modifier: Modifier = Modifier,
    currentPercentage: String
) {
    // Use remember so it won't be created several times
    val spinKitFactory = remember {
        { context: Context ->
            SpinKitView(
                ContextThemeWrapper(
                    context,
                    R.style.SpinKitView_Large_FadingCircle
                )
            )
        }
    }
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(.6f)),
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = spinKitFactory,
            modifier = Modifier.size(100.dp)
        )

        Text(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .padding(2.dp),
            text = "$currentPercentage%",
            fontSize = 20.sp,
            color = Color.White
        )
    }

}

@Composable
fun SplashSkipButton(
    skipButtonLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(0.dp, 10.dp, 10.dp, 0.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable(onClick = onClick)
            .width(80.dp)
            .height(30.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = skipButtonLabel,
            fontSize = 12.sp,
            color = Color.White,
        )
    }
}

@Composable
fun SplashAppVersion(
    modifier: Modifier = Modifier,
    version: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer.copy(.6f))
            .padding(horizontal = 9.dp, vertical = 3.dp)
    ) {
        Text(
            text = "v$version",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SplashViewPager(
    modifier: Modifier = Modifier,
    images: List<Any>,
    scrollInterval: Long,
    onScrollFinished: () -> Unit
) {
    val pagerState = rememberPagerState { images.size }
    Timber.d("SplashViewPager: Images: $images")

    LaunchedEffect(key1 = images) {
        if (images.isEmpty()) return@LaunchedEffect

        while (pagerState.currentPage < images.size - 1) {
            delay(scrollInterval)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }

        delay(scrollInterval)
        onScrollFinished()
    }

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false,
        modifier = modifier.fillMaxSize()
    ) { page ->
        GlideImage(
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = images[page],
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    MidasAndroidNativeRevampTheme {
        SplashScreenContent(
            modifier = Modifier,
            uiState = SplashScreenUiState(),
            onClickSkipBtn = { },
            onScrollFinished = { }
        )
    }
}