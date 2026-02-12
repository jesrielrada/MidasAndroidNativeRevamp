package com.prometheus_service.midas.core.presentation.features.second_stage.presentation

data class SecondStageScreenUiState(
    val isLoading: Boolean = false,
    val pinEnteredTexts: String = "",
    val pinHeaderValue: String = "",
    val pinFooterValue: String = "",
)
