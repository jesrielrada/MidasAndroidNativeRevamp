package com.prometheus_service.midas.core.presentation.main_screen.presentation.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.BiometricsTranslations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiometricsEnableDialog(
    onConfirm: () -> Unit,
    onDontShowAgain: () -> Unit,
    onDismiss: () -> Unit,
    translations: BiometricsTranslations
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = translations.dialogEnableTitle,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = translations.dialogEnableMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    TextButton(onClick = onDontShowAgain) {
                        Text(
                            fontSize = 12.sp,
                            text = translations.dialogNeutralBtnLabel,
                            maxLines = 1,
                        )
                    }

                    TextButton(onClick = onDismiss) {
                        Text(
                            fontSize = 12.sp,
                            text = translations.dialogNegativeBtnLabel,
                            maxLines = 1,
                        )
                    }

                    TextButton(onClick = onConfirm) {
                        Text(
                            fontSize = 12.sp,
                            text = translations.dialogPositiveBtnLabel,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BiometricsLoadingDialog(onDismissRequest: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun BiometricsErrorDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    translations: BiometricsTranslations
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(translations.currentDialogTitle) },
        text = {
            Text(
                text = translations.currentDialogMessage,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = translations.currentDialogButtonLabel,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
    )
}