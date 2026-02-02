package com.prometheus_service.midas.core.presentation.features.tutorial_screen.presentation.event

sealed class TutorialScreenEvent {
    object InitializeTutorialScreen: TutorialScreenEvent()
    object OnTutorialFinished: TutorialScreenEvent()
}


