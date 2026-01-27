package com.prometheus_service.midas.core.main_screen.event

sealed class MainScreenEvent {
    object HideSplashScreen: MainScreenEvent()
    object DisplayWebviewScreen: MainScreenEvent()

    object UpdatePWAReady: MainScreenEvent()
}