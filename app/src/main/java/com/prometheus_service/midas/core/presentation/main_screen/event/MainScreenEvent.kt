package com.prometheus_service.midas.core.presentation.main_screen.event

sealed class MainScreenEvent {
    object HideSplashScreen : MainScreenEvent()
    object HideTutorialScreen : MainScreenEvent()
    object DisplayWebviewScreen : MainScreenEvent()
    object UpdateWebviewReady : MainScreenEvent()
    object SyncSplashTutorialImages: MainScreenEvent()
    object InitializeApplication: MainScreenEvent()
}