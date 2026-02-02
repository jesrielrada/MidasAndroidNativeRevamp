package com.prometheus_service.midas.core.presentation.features.language_selection.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prometheus_service.midas.core.presentation.features.language_selection.event.LanguageSelectionEvent
import com.prometheus_service.midas.shared.theme.MidasAndroidNativeRevampTheme
import timber.log.Timber


@Composable
fun LanguageSelectionScreen(
    modifier: Modifier = Modifier,
    viewModel: LanguageSelectionViewModel = hiltViewModel(),
    onInitialized: (canDisplay: Boolean) -> Unit,
    onLanguageSelected: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        snapshotFlow { uiState.canDisplayScreen }
            .collect { canDisplay ->
                Timber.d("Calling initialized on language selection.. canDisplay: $canDisplay")
                onInitialized(canDisplay)
            }
    }

    LanguageSelectionScreenContent(
        modifier = modifier,
        uiState = uiState,
        onClick = {
            viewModel.onEvent(LanguageSelectionEvent.OnLanguageSelected(it))
            onLanguageSelected()
        }
    )
}

@Composable
fun LanguageSelectionScreenContent(
    modifier: Modifier = Modifier,
    uiState: LanguageSelectionUiState,
    onClick: (String) -> Unit
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    MaterialTheme.colorScheme.background
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        top = 88.dp,
                        bottom = 22.dp
                    ),
                text = uiState.header,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )

            uiState.supportedLocales.forEachIndexed { index, locale ->
                LanguageSelectionItem(
                    modifier = Modifier.padding(top = 10.dp),
                    language = locale,
                    onClick = { displayLocale ->
                        onClick(displayLocale)
                    }
                )
            }
        }
    }
}


@Composable
fun LanguageSelectionItem(
    modifier: Modifier = Modifier,
    language: String,
    onClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                onClick(language)
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = language,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LanguageSelectionScreenPreview() {
    MidasAndroidNativeRevampTheme {
        LanguageSelectionScreenContent(
            uiState = LanguageSelectionUiState(),
            onClick = {}
        )
    }
}