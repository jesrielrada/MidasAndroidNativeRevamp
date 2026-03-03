package com.prometheus_service.midas.core.presentation.main_screen.presentation.handler

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity.RESULT_CANCELED
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.biometric.BiometricPrompt.ERROR_CANCELED
import androidx.biometric.BiometricPrompt.ERROR_LOCKOUT
import androidx.biometric.BiometricPrompt.ERROR_LOCKOUT_PERMANENT
import androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON
import androidx.biometric.BiometricPrompt.ERROR_USER_CANCELED
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.prometheus_service.midas.cmspwaupdater.NewUpdateActivity
import com.prometheus_service.midas.core.presentation.features.biometrics.BiometricAuthenticator
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleAccountSelected
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleAccountSelectedAuthSucceed
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleAccountSelectionAuthCancelled
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleBiometricsAuthError
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandleBiometricsAuthResult
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.HandlePwaReady
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenEvent.ProcessGoogleLogin
import com.prometheus_service.midas.core.presentation.main_screen.event.MainScreenSideEffect
import com.prometheus_service.midas.core.presentation.main_screen.presentation.MainScreenViewModel
import com.prometheus_service.midas.core.presentation.main_screen.presentation.util.Constants
import com.prometheus_service.midas.core.presentation.util.GoogleAuthManager
import timber.log.Timber

@Composable
fun MainScreenEffectHandler(
    viewModel: MainScreenViewModel,
    context: Context,
    activity: FragmentActivity,
    authenticator: BiometricAuthenticator,
    googleAuthManager: GoogleAuthManager
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val appUpdateLauncher =
        rememberLauncherForActivityResult(contract = StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_CANCELED) {
                val data: Intent? = result.data
                if (data != null && data.hasExtra("required")) {
                    val required = data.getBooleanExtra("required", false)
                    if (required) {
                        (context as? android.app.Activity)?.finish()
                    }
                }
            }
        }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is MainScreenSideEffect.LaunchRequestPermission -> {
                    if (!viewModel.permissionManager.isStoragePermissionRationale(activity)) {
                        ActivityCompat.requestPermissions(
                            activity,
                            Array(1) { WRITE_EXTERNAL_STORAGE },
                            Constants.STORAGE_PERMISSION
                        )

                        viewModel.updateMainState { it.copy(remoteVersionInfo = effect.versionInfo) }

                    } else {
                        viewModel.updateMainState {
                            it.copy(
                                isAppLatest = true
                            )
                        }
                    }
                }

                is MainScreenSideEffect.LaunchUpdateActivity -> {
                    val intent = Intent(context, NewUpdateActivity::class.java)
                    intent.putExtra("version_info", effect.versionInfo)
                    appUpdateLauncher.launch(intent)
                }

                is MainScreenSideEffect.StartActionView -> {
                    Timber.d("Starting action view ...")
                    val intent = Intent(Intent.ACTION_VIEW, effect.url.toUri())
                    context.startActivity(intent)
                }

                is MainScreenSideEffect.DisplayBiometricAuthError -> {
                    val negative =
                        uiState.viewTranslations.biometricsTranslations.biometricErrorCancelled
                    val lockout =
                        uiState.viewTranslations.biometricsTranslations.biometricErrorLockout

                    val errorMessage = when (effect.code) {
                        ERROR_CANCELED,
                        ERROR_USER_CANCELED,
                        ERROR_NEGATIVE_BUTTON -> {
                            negative
                        }

                        ERROR_LOCKOUT,
                        ERROR_LOCKOUT_PERMANENT -> {
                            lockout
                        }

                        else -> effect.message
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

                is MainScreenSideEffect.DisplayBiometricSelectionList -> {
                    val cancelBtnLabel =
                        uiState.viewTranslations.biometricsTranslations.promptCancel
                    val selectAccountLabel =
                        uiState.viewTranslations.biometricsTranslations.dialogSelectAccount
                    MaterialAlertDialogBuilder(context).apply {
                        setTitle(selectAccountLabel)
                        setItems(effect.usernames?.toTypedArray()) { dialog, index ->
                            effect.usernames?.get(index)?.let {
                                viewModel.onEvent(HandleAccountSelected(it))
                            }
                        }
                        setNegativeButton(cancelBtnLabel) { _, _ ->
                            viewModel.onEvent(HandleAccountSelectionAuthCancelled)
                        }
                        show()
                    }
                }

                MainScreenSideEffect.DisplayBiometricSuccessEnrollment -> {
                    Toast.makeText(
                        context,
                        uiState.viewTranslations.biometricsTranslations.biometricToastMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MainScreenSideEffect.DisplayBiometricPrompt -> {
                    Timber.d("Displaying biometric prompt...")

                    val promptInfo = PromptInfo.Builder()
                        .setTitle(uiState.viewTranslations.biometricsTranslations.promptTitle)
                        .setNegativeButtonText(uiState.viewTranslations.biometricsTranslations.promptCancel)
                        .setConfirmationRequired(false)
                        .build()

                    authenticator.authenticate(
                        promptInfo = promptInfo,
                        cryptoObject = CryptoObject(effect.cipher),
                        onSuccess = { result ->
                            if (effect.isFromAccountSelection) {
                                Timber.d("Biometric authentication succeeded, is from account selection .. ")
                                viewModel.onEvent(HandleAccountSelectedAuthSucceed(result))
                            } else {
                                Timber.d("Biometric authentication succeeded, is not from account selection .. ")
                                viewModel.onEvent(HandleBiometricsAuthResult(result))
                            }
                        },
                        onError = { code, msg ->
                            viewModel.onEvent(HandleBiometricsAuthError(code = code, message = msg))
                        }
                    )

                }

                is MainScreenSideEffect.OnPwaReady -> {
                    Timber.d("PWA ready called on side effects, calling handle pwa ...")
                    viewModel.onEvent(HandlePwaReady(effect.data))
                }

                is MainScreenSideEffect.ClearGoogleCredential -> {
                    googleAuthManager.clearSession()
                }

                is MainScreenSideEffect.RequestGoogleLogin -> {
                    try {
                        val clientId = viewModel.googleClientId
                        val result = googleAuthManager.getGoogleCredential(clientId)

                        viewModel.onEvent(
                            ProcessGoogleLogin(
                                clientId = clientId,
                                response = result,
                                url = effect.url,
                            )
                        )
                    } catch (e: Exception) {
                        Timber.e("Creating credential manager failed... $e")
                    }
                }
            }
        }
    }
}