package com.prometheus_service.midas.core.presentation.features.splash_screen.presentation

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.event.SplashScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor() : ViewModel() {
    companion object {
        private const val TOTAL_TIME_LIMIT = 2 * 60 * 1000L // 2 minutes
        private const val INTERVAL = 200L  // 200ms
    }

    private val _uiState = MutableStateFlow(SplashScreenUiState())
    val uiState: StateFlow<SplashScreenUiState> = _uiState.asStateFlow()
    private val pausePercentageList = listOf(50, 99)
    private val timer = object : CountDownTimer(
        TOTAL_TIME_LIMIT,
        INTERVAL
    ) {
        override fun onTick(millisUntilFinished: Long) {
            if (uiState.value.isTimerRunning) {
                onEvent(SplashScreenEvent.UpdateProgress)
                if (uiState.value.currentPauseIndex < pausePercentageList.size &&
                    uiState.value.currentPercentage == pausePercentageList[
                        uiState.value.currentPauseIndex
                    ]
                ) {
                    onEvent(SplashScreenEvent.PauseTimer)
                }
            }
        }

        override fun onFinish() {
            onEvent(SplashScreenEvent.CancelTimer)
        }
    }

    init {
        onEvent(SplashScreenEvent.StartTimer)
    }

    fun onEvent(event: SplashScreenEvent) {
        when (event) {
            SplashScreenEvent.IncrementPauseIndex -> {
                if (uiState.value.currentPauseIndex < 1) {
                    _uiState.update {
                        it.copy(
                            currentPauseIndex = it.currentPauseIndex + 1
                        )
                    }
                }
                _uiState.update {
                    it.copy(
                        currentPercentage = pausePercentageList[uiState.value.currentPauseIndex - 1],
                        isTimerRunning = true
                    )
                }
            }

            SplashScreenEvent.PauseTimer -> {
                _uiState.update {
                    it.copy(isTimerRunning = false)
                }
            }

            SplashScreenEvent.CancelTimer -> {
                _uiState.update {
                    it.copy(isTimerRunning = false)
                }
                timer.cancel()
            }

            SplashScreenEvent.StartTimer -> {
                timer.cancel()
                timer.start()
                _uiState.update {
                    it.copy(
                        isTimerRunning = true,
                        currentPercentage = 0,
                        currentPauseIndex = 0,
                        isProgressVisible = true
                    )
                }
            }

            SplashScreenEvent.StopSplashProgress -> {
                timer.cancel()
                _uiState.update {
                    it.copy(
                        isTimerRunning = false,
                        currentPercentage = 0,
                        currentPauseIndex = 0,
                        isProgressVisible = true
                    )
                }
            }

            SplashScreenEvent.UpdateProgress -> {
                _uiState.update {
                    it.copy(currentPercentage = it.currentPercentage + 1)
                }
            }

            SplashScreenEvent.OnScrollFinished -> {
                _uiState.update {
                    it.copy(
                        isScrollFinished = true
                    )
                }
            }

            SplashScreenEvent.DisplaySkipButton -> {
                _uiState.update {
                    it.copy(
                        isSkipVisible = true
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}