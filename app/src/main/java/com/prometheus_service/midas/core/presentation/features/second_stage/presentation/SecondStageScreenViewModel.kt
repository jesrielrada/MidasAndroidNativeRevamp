package com.prometheus_service.midas.core.presentation.features.second_stage.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.FlavorConfig
import com.prometheus_service.midas.core.domain.features.second_stage.model.SecondStageModel
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.CacheSecondStageConfig
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.DecryptPin
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.EncryptPin
import com.prometheus_service.midas.core.domain.features.second_stage.use_cases.GetSecondStageConfig
import com.prometheus_service.midas.core.presentation.features.second_stage.presentation.state.SecondStageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecondStageScreenViewModel @Inject constructor(
    private val getSecondStageConfig: GetSecondStageConfig,
    private val cacheSecondStageConfig: CacheSecondStageConfig,
    private val encryptPin: EncryptPin,
    private val decryptPin: DecryptPin
) : ViewModel() {

    private val _uiState = MutableStateFlow(SecondStageScreenUiState())
    val uiState: StateFlow<SecondStageScreenUiState> = _uiState.asStateFlow()
    private val key = FlavorConfig.OPERATOR_ID
    private val clientSecret = FlavorConfig.CLIENT_SECRET


    init {
        viewModelScope.launch {
            val config = getSecondStageConfig.invoke().firstOrNull()
            val userEnabled = config != null && config.isUserEnabled != null && config.isUserEnabled

            if (userEnabled) {
                val encryptedPin = config.pin
                val decryptedPin = decryptPin.invoke(
                    key = key,
                    clientSecret = clientSecret,
                    encryptedPin = encryptedPin
                )

                _uiState.update {
                    it.copy(
                        pinValue = decryptedPin ?: "",
                        currentState = SecondStageState.DisplayLockScreen,
                        pinHeaderValue = uiState.value.translations.pinHeaderEnterPin,
                        pinFooterValue = remainingAttemptsString()
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        currentState = SecondStageState.DisplayCreatePin,
                        pinHeaderValue = uiState.value.translations.pinHeaderCreatePin,
                        pinFooterValue = uiState.value.translations.pinFooterCancelSettings
                    )
                }
            }
        }
    }

    fun onCompleteText(value: String) {
        when (uiState.value.currentState) {
            SecondStageState.DisplayConfirmPin -> {
                if (uiState.value.pinEnteredValue == uiState.value.pinValue) {
                    viewModelScope.launch {
                        val value = uiState.value.pinValue
                        val encryptedPin = encryptPin.invoke(
                            key = key,
                            clientSecret = clientSecret,
                            value = value
                        )

                        if (encryptedPin != null) {
                            cacheSecondStageConfig.invoke(
                                SecondStageModel(
                                    isUserEnabled = true,
                                    pin = encryptedPin
                                )
                            )
                        }

                        _uiState.update {
                            it.copy(
                                shouldHideScreen = true
                            )
                        }
                    }

                } else {
                    _uiState.update {
                        it.copy(
                            currentState = SecondStageState.DisplayIncorrectPinCreateNew,
                            pinHeaderValue = uiState.value.translations.pinHeaderIncorrectPin,
                            pinFooterValue = uiState.value.translations.pinFooterCancelSettings,
                            pinEnteredValue = "",
                            pinValue = ""
                        )
                    }
                }
            }

            SecondStageState.DisplayCreatePin -> {
                _uiState.update {
                    it.copy(
                        pinEnteredValue = "",
                        pinValue = value,
                        currentState = SecondStageState.DisplayConfirmPin,
                        pinHeaderValue = uiState.value.translations.pinHeaderConfirmPin,
                        pinFooterValue = uiState.value.translations.pinFooterCancelSettings
                    )
                }
            }

            SecondStageState.DisplayIncorrectPin -> {
                if (uiState.value.pinEnteredValue == uiState.value.pinValue) {
                    _uiState.update {
                        it.copy(
                            shouldHideScreen = true
                        )
                    }
                } else {
                    val tryCount = uiState.value.tryCount + 1
                    val remainingAttempts = uiState.value.remainingAttempts - 1

                    _uiState.update {
                        it.copy(
                            currentState = SecondStageState.DisplayIncorrectPin,
                            pinHeaderValue = uiState.value.translations.pinHeaderIncorrectPin,
                            tryCount = tryCount,
                            remainingAttempts = remainingAttempts,
                        )
                    }

                    if (uiState.value.tryCount > 2) {
                        _uiState.update {
                            it.copy(
                                onMaxAttempt = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                tryCount = uiState.value.tryCount,
                                remainingAttempts = uiState.value.remainingAttempts,
                                pinHeaderValue = uiState.value.translations.pinHeaderIncorrectPin,
                                pinFooterValue = remainingAttemptsString(),
                                pinEnteredValue = ""
                            )
                        }
                    }
                }
            }

            SecondStageState.DisplayIncorrectPinCreateNew -> {
                _uiState.update {
                    it.copy(
                        pinHeaderValue = uiState.value.translations.pinHeaderConfirmPin,
                        pinFooterValue = uiState.value.translations.pinFooterCancelSettings,
                        currentState = SecondStageState.DisplayConfirmPin,
                        pinEnteredValue = "",
                        pinValue = value
                    )
                }
            }

            SecondStageState.DisplayLockScreen -> {
                if (uiState.value.pinEnteredValue == uiState.value.pinValue) {
                    _uiState.update {
                        it.copy(
                            shouldHideScreen = true
                        )
                    }
                } else {
                    val tryCount = uiState.value.tryCount + 1
                    val remainingAttempts = uiState.value.remainingAttempts - 1

                    _uiState.update {
                        it.copy(
                            tryCount = tryCount,
                            remainingAttempts = remainingAttempts,
                            currentState = SecondStageState.DisplayIncorrectPin,
                            pinHeaderValue = uiState.value.translations.pinHeaderIncorrectPin,
                            pinFooterValue = remainingAttemptsString(),
                            pinEnteredValue = ""
                        )
                    }
                }
            }
        }
    }

    fun updateState(state: (SecondStageScreenUiState) -> SecondStageScreenUiState) {
        _uiState.update(state)
    }

    private fun remainingAttemptsString(): String {
        return "${uiState.value.translations.pinFooterRemainingAttempts} (${uiState.value.remainingAttempts})"
    }


}