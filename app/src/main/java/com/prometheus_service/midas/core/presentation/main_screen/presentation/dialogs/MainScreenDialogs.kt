package com.prometheus_service.midas.core.presentation.main_screen.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.prometheus_service.midas.core.presentation.main_screen.presentation.MainScreenUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkErrorDialog(
    uiState: MainScreenUiState,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
            .zIndex(5f),
        onDismissRequest = {},
        content = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        color = MaterialTheme.colorScheme.onSecondary,
                        text = uiState.viewTranslations.mainScreenTranslations.networkErrorMessage
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onDismiss() },
                    color = MaterialTheme.colorScheme.onSecondary,
                    text = uiState.viewTranslations.mainScreenTranslations.exitButtonLabel
                )
            }
        }
    )
}


@Composable
fun DefaultErrorDialog(
    onConfirm: () -> Unit,
    dialogMessage: String,
    dialogBtn: String
) {
    AlertDialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = {},
        text = {
            Text(
                text = dialogMessage,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = dialogBtn,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    )
}