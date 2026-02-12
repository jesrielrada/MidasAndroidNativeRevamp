package com.prometheus_service.midas.core.domain.features.second_stage.model

import kotlinx.serialization.Serializable

@Serializable
data class SecondStageModel(
    val isUserEnabled: Boolean? = null,
    val isCmsboEnabled: Boolean? = null,
    val pin: String? = null,
    val credentials: String? = null
)
