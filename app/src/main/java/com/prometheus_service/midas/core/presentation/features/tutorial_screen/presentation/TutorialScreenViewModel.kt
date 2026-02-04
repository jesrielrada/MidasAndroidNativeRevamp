package com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prometheus_service.midas.core.domain.features.splash_tutorial.use_case.GetSplashTutorialData
import com.prometheus_service.midas.core.domain.shared.app_config.model.AppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.CacheAppConfigModel
import com.prometheus_service.midas.core.domain.shared.app_config.use_case.GetAppConfigModel
import com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.event.TutorialScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialScreenViewModel @Inject constructor(
    private val getSplashTutorialData: GetSplashTutorialData,
    private val cacheAppConfigModel: CacheAppConfigModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(TutorialScreeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        onEvent(TutorialScreenEvent.InitializeTutorialImages)
    }

    fun onEvent(event: TutorialScreenEvent) {
        when (event) {
            TutorialScreenEvent.InitializeTutorialImages -> {
                viewModelScope.launch {
                    val images = getSplashTutorialData.invoke().first().tutorialImages
                    _uiState.update {
                        it.copy(
                            images = images
                        )
                    }
                }
            }

            TutorialScreenEvent.OnTutorialFinished -> {
                viewModelScope.launch {
                    cacheAppConfigModel.invoke(
                        AppConfigModel(
                            isTutorialDisplayed = true
                        )
                    )
                }
            }
        }
    }
}