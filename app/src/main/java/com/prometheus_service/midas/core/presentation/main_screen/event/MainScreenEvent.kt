package com.prometheus_service.midas.core.presentation.main_screen.event

sealed class MainScreenEvent {
    object HideSplashScreen : MainScreenEvent()
    object HideTutorialScreen : MainScreenEvent()
    object HideLanguageSelectionScreen: MainScreenEvent()
    object DisplayWebviewScreen : MainScreenEvent()
    object OnWebviewReady : MainScreenEvent()
    object InitializeApplication : MainScreenEvent()
    object SyncRemoteData : MainScreenEvent()
}