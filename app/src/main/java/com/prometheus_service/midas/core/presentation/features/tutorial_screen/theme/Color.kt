package com.prometheus_service.midas.core.presentation.features.tutorial_screen.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


val TutorialContainerDarkColor = Color(0XFF0E0E0E)
val TutorialContainerLightColor = Color(0XFFF0F2F4)
val TutorialIndicatorSelectedDarkColor = Color(0xFFDADEE3)
val TutorialIndicatorUnSelectedDarkColor = Color(0xff636C77)
val TutorialIndicatorSelectedLightColor = Color(0XFF212121)
val TutorialIndicatorUnSelectedLightColor = Color(0XFFA9B0BB)
val TutorialNextButtonDarkColor = Color(0xff404954)
val TutorialNextButtonLightColor = Color(0XFFDADEE3)
val TutorialNextButtonFinishColor = Color(0xffC4271C)
val TutorialNextButtonOnContainerDarkColor = Color(0xffF6C244)
val TutorialNextButtonOnContainerLightColor = Color(0XFFC4271C)
val TutorialNextButtonOnContainerFinishDarkColor = Color.White
val TutorialNextButtonOnContainerFinishLightColor = Color.White


val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        tutorialContainer = TutorialContainerLightColor,
        tutorialIndicatorSelected = TutorialIndicatorSelectedLightColor,
        tutorialIndicatorUnSelected = TutorialIndicatorUnSelectedLightColor,
        tutorialOnButtonContainer = TutorialNextButtonOnContainerLightColor,
        tutorialOnButtonContainerFinish = TutorialNextButtonOnContainerFinishLightColor,
        tutorialNextButton = TutorialNextButtonLightColor,
        tutorialFinishButton = TutorialNextButtonFinishColor
    )
}

val DarkExtendedColors = ExtendedColors(
    tutorialContainer = TutorialContainerDarkColor,
    tutorialIndicatorSelected = TutorialIndicatorSelectedDarkColor,
    tutorialIndicatorUnSelected = TutorialIndicatorUnSelectedDarkColor,
    tutorialOnButtonContainer = TutorialNextButtonOnContainerDarkColor,
    tutorialOnButtonContainerFinish = TutorialNextButtonOnContainerFinishDarkColor,
    tutorialNextButton = TutorialNextButtonDarkColor,
    tutorialFinishButton = TutorialNextButtonFinishColor
)


val LightExtendedColors = ExtendedColors(
    tutorialContainer = TutorialContainerLightColor,
    tutorialIndicatorSelected = TutorialIndicatorSelectedLightColor,
    tutorialIndicatorUnSelected = TutorialIndicatorUnSelectedLightColor,
    tutorialOnButtonContainer = TutorialNextButtonOnContainerLightColor,
    tutorialOnButtonContainerFinish = TutorialNextButtonOnContainerFinishLightColor,
    tutorialNextButton = TutorialNextButtonLightColor,
    tutorialFinishButton = TutorialNextButtonFinishColor
)
