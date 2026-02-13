package com.prometheus_service.midas.core.presentation.features.second_stage.presentation.state

sealed class SecondStageState {
    object DisplayCreatePin : SecondStageState()
    object DisplayIncorrectPinCreateNew : SecondStageState()
    object DisplayIncorrectPin : SecondStageState()
    object DisplayConfirmPin : SecondStageState()
    object DisplayLockScreen : SecondStageState()
}