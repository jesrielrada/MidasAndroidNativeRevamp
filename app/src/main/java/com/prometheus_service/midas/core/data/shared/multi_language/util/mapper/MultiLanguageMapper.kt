package com.prometheus_service.midas.core.data.shared.multi_language.util.mapper

import com.prometheus_service.midas.core.data.shared.multi_language.remote.model.MultiLanguageDto
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedBiometricsTranslations
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedErrorTranslations
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedFeatureSettings
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedGeneralTranslations
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedModels
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedOsVersionTranslations
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedPinLockTranslations
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedPopupTranslations
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedSplashTranslations
import com.prometheus_service.midas.core.domain.shared.multi_language.model.LocalizedTutorialTranslations
import com.prometheus_service.midas.core.domain.shared.multi_language.model.MultiLanguageModel

fun MultiLanguageDto.toDomain(locale: String): MultiLanguageModel {
    val data = this.data.data
    return MultiLanguageModel(
        mapOf(
            locale to LocalizedModels(
                featureSettings = LocalizedFeatureSettings(
                    minOsVersionIsEnabled = data.osVersion.minOsVersionIsEnabled,
                    minOsVersionAndroid = data.osVersion.minOsVersionAndroid,
                    pinlockEnabled = data.pinLock.pinLockIsEnabled,
                    biometricsEnabled = data.biometrics.biometricsIsEnabled
                ),
                generalMessages = LocalizedGeneralTranslations(
                    retry = data.generalMessages.retryMessage,
                    exitApp = data.generalMessages.exitApplicationMessage,
                    ok = data.generalMessages.okMessage,
                    download = data.generalMessages.downloadMessage,
                    openingPage = data.generalMessages.openingPageMessage,
                    maintenanceDialog = data.generalMessages.maintenanceDialogMessage,
                    geoblockedDialog = data.generalMessages.geoblockedDialogMessage,
                    submit = data.generalMessages.submitMessage,
                    imageSavedMessage = data.generalMessages.imageSavedMessage
                ),
                errorMessages = LocalizedErrorTranslations(
                    network = data.errorMessages.networkError,
                    fetchDomain = data.errorMessages.fetchDomainError,
                    homepage = data.errorMessages.homepageError
                ),
                popupMessages = LocalizedPopupTranslations(
                    popupExitMessage = data.popupMessages.popupExitMessage,
                    popupYes = data.popupMessages.popupYes,
                    popupNo = data.popupMessages.popupNo
                ),
                osVersionSettings = LocalizedOsVersionTranslations(
                    minOsVersionTitle = data.osVersion.minOsVersionTitle,
                    minOsVersionMessage = data.osVersion.minOsVersionMessage,
                    minOsVersionInstruction = data.osVersion.minOsVersionInstruction,
                    minOsVersionOk = data.osVersion.minOsVersionOk
                ),
                splashTranslations = LocalizedSplashTranslations(
                    splashSkipButton = data.splash.splashSkipButton
                ),
                tutorialTranslations = LocalizedTutorialTranslations(
                    tutorialNextButton = data.tutorial.tutorialNextButton,
                    tutorialEndButton = data.tutorial.tutorialEndButton
                ),
                pinLockTranslations = LocalizedPinLockTranslations(
                    pinLockScreen = data.pinLock.pinLockScreen,
                    pinCreate = data.pinLock.pinCreate,
                    pinConfirm = data.pinLock.pinConfirm,
                    pinEnter = data.pinLock.pinEnter,
                    pinIncorrect = data.pinLock.pinIncorrect,
                    pinIncorrectNew = data.pinLock.pinIncorrectNew,
                    pinConfirmOld = data.pinLock.pinConfirmOld,
                    pinConfirmNew = data.pinLock.pinConfirmNew,
                    pinConfirmVerify = data.pinLock.pinConfirmVerify,
                    pinConfirmOldIncorrect = data.pinLock.pinConfirmOldIncorrect,
                    pinForgotAlert = data.pinLock.pinForgotAlert,
                    pinForgotButtonText = data.pinLock.pinForgotButtonText,
                    pinForgotButtonCancel = data.pinLock.pinForgotButtonCancel,
                    pinAttemptsText = data.pinLock.pinAttemptsText
                ),
                biometricsTranslations = LocalizedBiometricsTranslations(
                    promptInfoTitle = data.biometrics.promptInfoTitle,
                    promptInfoCancel = data.biometrics.promptInfoCancel,
                    biometricsSelectAccount = data.biometrics.biometricsSelectAccount,
                    biometricsErrorHwUnavailable = data.biometrics.biometricsErrorHwUnavailable,
                    biometricsErrorNoneEnrolled = data.biometrics.biometricsErrorNoneEnrolled,
                    biometricsErrorNoHardware = data.biometrics.biometricsErrorNoHardware,
                    biometricsErrorSecurityUpdate = data.biometrics.biometricsErrorSecurityUpdate,
                    biometricsAlertTitle = data.biometrics.biometricsAlertTitle,
                    biometricsAlertMessage = data.biometrics.biometricsAlertMessage,
                    biometricsAlertPositiveBtn = data.biometrics.biometricsAlertPositiveBtn,
                    biometricsAlertNegativeBtn = data.biometrics.biometricsAlertNegativeBtn,
                    biometricsAlertNeutralBtn = data.biometrics.biometricsAlertNeutralBtn,
                    biometricsToastMessage = data.biometrics.biometricsToastMessage,
                    biometricsErrorSetupRequired = data.biometrics.biometricsErrorSetupRequired,
                    biometricsErrorCanceled = data.biometrics.biometricsErrorCanceled,
                    biometricsErrorDefault = data.biometrics.biometricsErrorDefault,
                    biometricsErrorLockout = data.biometrics.biometricsErrorLockout,
                    biometricsErrorLoginFailed = data.biometrics.biometricsErrorLoginFailed
                )
            )
        )
    )
}