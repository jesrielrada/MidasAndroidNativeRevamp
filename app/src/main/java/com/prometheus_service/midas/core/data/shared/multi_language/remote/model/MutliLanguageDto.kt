package com.prometheus_service.midas.core.data.shared.multi_language.remote.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import javax.annotation.processing.Generated

@Keep
@Generated
data class 	MultiLanguageDto (
    @SerializedName("data") val data : Data,
    @SerializedName("statusCode") val statusCode : Int,
    @SerializedName("statusText") val statusText : String,
    @SerializedName("message") val message : String
)
@Keep
@Generated
data class Data(
    @SerializedName("data") val data: MultiLanguageData
)
@Keep
@Generated
data class MultiLanguageData(
    @SerializedName("error_messages") val errorMessages: ErrorMessages,
    @SerializedName("general_messages") val generalMessages: GeneralMessages,
    @SerializedName("popup_messages") val popupMessages: PopupMessages,
    @SerializedName("os_version") val osVersion: OsVersion,
    @SerializedName("pin_lock") val pinLock: PinLockRF,
    @SerializedName("tutorial") val tutorial: Tutorial,
    @SerializedName("biometrics") val biometrics: Biometrics,
    @SerializedName("splash") val splash: Splash,
)
@Keep
@Generated
data class GeneralMessages(
    @SerializedName("retry_message") val retryMessage: String,
    @SerializedName("exit_application_message") val exitApplicationMessage: String,
    @SerializedName("try_again_message") val tryAgainMessage: String,
    @SerializedName("ok_message") val okMessage: String,
    @SerializedName("download_message") val downloadMessage: String,
    @SerializedName("opening_page_message") val openingPageMessage: String,
    @SerializedName("maintenance_dialog_message") val maintenanceDialogMessage: String,
    @SerializedName("geoblocked_dialog_message") val geoblockedDialogMessage: String,
    @SerializedName("mobile_number_placeholder_message") val mobileNumberPlaceholder: String,
    @SerializedName("submit_message") val submitMessage: String,
    @SerializedName("play_for_real_description") val playForRealDescription: String,
    @SerializedName("affiliate_message") val affiliateMessage: String,
    @SerializedName("image_saved_message") val imageSavedMessage: String
)
@Keep
@Generated
data class ErrorMessages(
    @SerializedName("network_error") val networkError: String,
    @SerializedName("fetch_domain_error") val fetchDomainError: String,
    @SerializedName("homepage_error") val homepageError: String,
)
@Keep
@Generated
data class OsVersion(
    @SerializedName("min_os_version_is_enabled") val minOsVersionIsEnabled: Boolean,
    @SerializedName("min_os_version_android") val minOsVersionAndroid: Double,
    @SerializedName("min_os_version_title") val minOsVersionTitle: String,
    @SerializedName("min_os_version_message") val minOsVersionMessage: String,
    @SerializedName("min_os_version_instruction") val minOsVersionInstruction: String,
    @SerializedName("min_os_version_ok") val minOsVersionOk: String,
)
@Keep
@Generated
data class PinLockRF(
    @SerializedName("pin_lock_is_enabled") val pinLockIsEnabled: Boolean,
    @SerializedName("pin_lock_screen") val pinLockScreen: String,
    @SerializedName("pin_create") val pinCreate: String,
    @SerializedName("pin_confirm") val pinConfirm: String,
    @SerializedName("pin_enter") val pinEnter: String,
    @SerializedName("pin_incorrect") val pinIncorrect: String,
    @SerializedName("pin_incorrect_new") val pinIncorrectNew: String,
    @SerializedName("pin_confirm_old") val pinConfirmOld: String,
    @SerializedName("pin_confirm_new") val pinConfirmNew: String,
    @SerializedName("pin_confirm_verify") val pinConfirmVerify: String,
    @SerializedName("pin_confirm_old_incorrect") val pinConfirmOldIncorrect: String,
    @SerializedName("pin_forgot_alert") val pinForgotAlert: String,
    @SerializedName("pin_forgot_button_text") val pinForgotButtonText: String,
    @SerializedName("pin_forgot_button_cancel") val pinForgotButtonCancel: String,
    @SerializedName("pin_attempts_text") val pinAttemptsText: String,
)
@Keep
@Generated
data class PopupMessages(
    @SerializedName("popup_exit_message") val popupExitMessage: String,
    @SerializedName("popup_yes") val popupYes: String,
    @SerializedName("popup_no") val popupNo: String,
)
@Keep
@Generated
data class Splash(
    @SerializedName("splash_skip_button") val splashSkipButton: String
)
@Keep
@Generated
data class Tutorial(
    @SerializedName("tutorial_next_button") val tutorialNextButton: String,
    @SerializedName("tutorial_end_button") val tutorialEndButton: String
)
@Keep
@Generated
data class Biometrics(
    @SerializedName("biometrics_is_enabled") val biometricsIsEnabled: Boolean,
    @SerializedName("prompt_info_title") val promptInfoTitle: String,
    @SerializedName("prompt_info_cancel") val promptInfoCancel: String,
    @SerializedName("biometrics_select_account") val biometricsSelectAccount: String,
    @SerializedName("biometrics_error_hw_unavailable") val biometricsErrorHwUnavailable: String,
    @SerializedName("biometrics_error_none_enrolled") val biometricsErrorNoneEnrolled: String,
    @SerializedName("biometrics_error_no_hardware") val biometricsErrorNoHardware: String,
    @SerializedName("biometrics_error_security_update") val biometricsErrorSecurityUpdate: String,
    @SerializedName("biometrics_alert_title") val biometricsAlertTitle: String,
    @SerializedName("biometrics_alert_message") val biometricsAlertMessage: String,
    @SerializedName("biometrics_alert_positive_btn") val biometricsAlertPositiveBtn: String,
    @SerializedName("biometrics_alert_negative_btn") val biometricsAlertNegativeBtn: String,
    @SerializedName("biometrics_alert_neutral_btn") val biometricsAlertNeutralBtn: String,
    @SerializedName("biometrics_toast_message") val biometricsToastMessage: String,
    @SerializedName("biometrics_error_setup_required") val biometricsErrorSetupRequired: String,
    @SerializedName("biometrics_error_canceled") val biometricsErrorCanceled: String,
    @SerializedName("biometrics_error_default") val biometricsErrorDefault: String,
    @SerializedName("biometrics_error_lockout") val biometricsErrorLockout: String,
    @SerializedName("biometrics_error_login_failed") val biometricsErrorLoginFailed: String
)
