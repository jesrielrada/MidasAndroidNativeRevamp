package com.prometheus_service.midas.core.domain.features.multi_language.model

import kotlinx.serialization.Serializable

@Serializable
data class MultiLanguageModel(
    val localizedModels: Map<String, LocalizedModels> = emptyMap()
)
@Serializable
data class LocalizedModels(
    val featureSettings: LocalizedFeatureSettings,
    val generalMessages: LocalizedGeneralTranslations,
    val errorMessages: LocalizedErrorTranslations,
    val osVersionSettings: LocalizedOsVersionTranslations,
    val splashTranslations: LocalizedSplashTranslations,
    val tutorialTranslations: LocalizedTutorialTranslations,
    val pinLockTranslations: LocalizedPinLockTranslations,
    val biometricsTranslations: LocalizedBiometricsTranslations
)
@Serializable
data class LocalizedFeatureSettings(
    val minOsVersionIsEnabled: Boolean,
    val minOsVersionAndroid: Double,
    val pinlockEnabled: Boolean,
    val biometricsEnabled: Boolean
)
@Serializable
data class LocalizedGeneralTranslations(
    val retry: String,
    val exitApp: String,
    val ok: String,
    val download: String,
    val openingPage: String,
    val maintenanceDialog: String,
    val geoblockedDialog: String,
    val submit: String?,
    val imageSavedMessage: String?
)
@Serializable
data class LocalizedErrorTranslations(
    val network: String,
    val fetchDomain: String,
    val homepage: String,
)
@Serializable
data class LocalizedOsVersionTranslations(
    val minOsVersionTitle: String,
    val minOsVersionMessage: String,
    val minOsVersionInstruction: String,
    val minOsVersionOk: String
)
@Serializable
data class LocalizedSplashTranslations(
    val splashSkipButton: String
)
@Serializable
data class LocalizedTutorialTranslations(
    val tutorialNextButton: String,
    val tutorialEndButton: String,
)
@Serializable
data class LocalizedPinLockTranslations(
    val pinLockScreen: String,
    val pinCreate: String,
    val pinConfirm: String,
    val pinEnter: String,
    val pinIncorrect: String,
    val pinIncorrectNew: String,
    val pinConfirmOld: String,
    val pinConfirmNew: String,
    val pinConfirmVerify: String,
    val pinConfirmOldIncorrect: String,
    val pinForgotAlert: String,
    val pinForgotButtonText: String,
    val pinForgotButtonCancel: String,
    val pinAttemptsText: String,
)
@Serializable
data class LocalizedBiometricsTranslations(
    val promptInfoTitle: String,
    val promptInfoCancel: String,
    val biometricsSelectAccount: String,
    val biometricsErrorHwUnavailable: String,
    val biometricsErrorNoneEnrolled: String,
    val biometricsErrorNoHardware: String,
    val biometricsErrorSecurityUpdate: String,
    val biometricsAlertTitle: String,
    val biometricsAlertMessage: String,
    val biometricsAlertPositiveBtn: String,
    val biometricsAlertNegativeBtn: String,
    val biometricsAlertNeutralBtn: String,
    val biometricsToastMessage: String,
    val biometricsErrorSetupRequired: String,
    val biometricsErrorCanceled: String,
    val biometricsErrorDefault: String,
    val biometricsErrorLockout: String,
    val biometricsErrorLoginFailed: String
)
