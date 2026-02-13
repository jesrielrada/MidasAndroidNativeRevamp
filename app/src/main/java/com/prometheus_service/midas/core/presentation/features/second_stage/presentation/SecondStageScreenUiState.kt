package com.prometheus_service.midas.core.presentation.features.second_stage.presentation

import com.prometheus_service.midas.core.presentation.features.second_stage.presentation.state.SecondStageState
import com.prometheus_service.midas.core.presentation.main_screen.presentation.model.SecondStageTranslations

data class SecondStageScreenUiState(
    val tryCount: Int = 0,
    val remainingAttempts: Int = 3,
    val pinEnteredValue: String = "",
    val pinValue: String = "",
    val pinHeaderValue: String = "",
    val pinFooterValue: String = "",
    val isFooterClickable: Boolean = false,
    val shouldHideScreen: Boolean = false,
    val onMaxAttempt: Boolean = false,
    val currentState: SecondStageState = SecondStageState.DisplayCreatePin,
    val translations: SecondStageTranslations = SecondStageTranslations()
)
