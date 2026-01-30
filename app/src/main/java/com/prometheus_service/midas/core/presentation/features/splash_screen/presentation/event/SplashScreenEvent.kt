package com.prometheus_service.midas.core.presentation.features.splash_screen.presentation.event

sealed class SplashScreenEvent {
    object StartTimer : SplashScreenEvent()
    object StopSplashProgress : SplashScreenEvent()
    object PauseTimer : SplashScreenEvent()
    object CancelTimer : SplashScreenEvent()
    object IncrementPauseIndex : SplashScreenEvent()
    object UpdateProgress : SplashScreenEvent()
    object DisplaySkipButton : SplashScreenEvent()
}


