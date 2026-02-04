package com.prometheus_service.midas

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
import kotlinx.serialization.Serializable


fun DefaultLocalizedModels.toDomain(): LocalizedModels {
    val featureSettings = LocalizedFeatureSettings(
        minOsVersionIsEnabled = this.featureSettings.minOsVersionIsEnabled,
        minOsVersionAndroid = this.featureSettings.minOsVersionAndroid,
        pinlockEnabled = this.featureSettings.pinlockEnabled,
        biometricsEnabled = this.featureSettings.biometricsEnabled
    )

    val generalMessages = LocalizedGeneralTranslations(
        retry = this.generalMessages.retry,
        exitApp = this.generalMessages.exitApp,
        ok = this.generalMessages.ok,
        download = this.generalMessages.download,
        openingPage = this.generalMessages.openingPage,
        maintenanceDialog = this.generalMessages.maintenanceDialog,
        geoblockedDialog = this.generalMessages.geoblockedDialog,
        submit = "",
        imageSavedMessage = ""
    )

    val errorMessages = LocalizedErrorTranslations(
        network = this.errorMessages.network,
        fetchDomain = this.errorMessages.fetchDomain,
        homepage = this.errorMessages.homepage
    )

    val osVersionSettings = LocalizedOsVersionTranslations(
        minOsVersionTitle = this.osVersionSettings.minOsVersionTitle,
        minOsVersionMessage = this.osVersionSettings.minOsVersionMessage,
        minOsVersionInstruction = this.osVersionSettings.minOsVersionInstruction,
        minOsVersionOk = this.osVersionSettings.minOsVersionOk
    )

    val splashTranslations = LocalizedSplashTranslations(
        splashSkipButton = this.splashTranslations.splashSkipButton
    )

    val tutorialTranslations = LocalizedTutorialTranslations(
        tutorialNextButton = this.tutorialTranslations.tutorialNextButton,
        tutorialEndButton = this.tutorialTranslations.tutorialEndButton
    )

    val biometricsTranslations = LocalizedBiometricsTranslations(
        promptInfoTitle = this.biometricsTranslations.promptInfoTitle,
        promptInfoCancel = this.biometricsTranslations.promptInfoCancel,
        biometricsSelectAccount = this.biometricsTranslations.biometricsSelectAccount,
        biometricsErrorHwUnavailable = this.biometricsTranslations.biometricsErrorHwUnavailable,
        biometricsErrorNoneEnrolled = this.biometricsTranslations.biometricsErrorNoneEnrolled,
        biometricsErrorNoHardware = this.biometricsTranslations.biometricsErrorNoHardware,
        biometricsErrorSecurityUpdate = this.biometricsTranslations.biometricsErrorSecurityUpdate,
        biometricsAlertTitle = this.biometricsTranslations.biometricsAlertTitle,
        biometricsAlertMessage = this.biometricsTranslations.biometricsAlertMessage,
        biometricsAlertPositiveBtn = this.biometricsTranslations.biometricsAlertPositiveBtn,
        biometricsAlertNegativeBtn = this.biometricsTranslations.biometricsAlertNegativeBtn,
        biometricsAlertNeutralBtn = this.biometricsTranslations.biometricsAlertNeutralBtn,
        biometricsToastMessage = this.biometricsTranslations.biometricsToastMessage,
        biometricsErrorSetupRequired = this.biometricsTranslations.biometricsErrorSetupRequired,
        biometricsErrorCanceled = this.biometricsTranslations.biometricsErrorCanceled,
        biometricsErrorDefault = this.biometricsTranslations.biometricsErrorDefault,
        biometricsErrorLockout = this.biometricsTranslations.biometricsErrorLockout,
        biometricsErrorLoginFailed = this.biometricsTranslations.biometricsErrorLoginFailed
    )

    val pinLockTranslations = LocalizedPinLockTranslations(
        pinLockScreen = this.pinLockTranslations.pinLockScreen,
        pinCreate = this.pinLockTranslations.pinCreate,
        pinConfirm = this.pinLockTranslations.pinConfirm,
        pinEnter = this.pinLockTranslations.pinEnter,
        pinIncorrect = this.pinLockTranslations.pinIncorrect,
        pinIncorrectNew = this.pinLockTranslations.pinIncorrectNew,
        pinConfirmOld = this.pinLockTranslations.pinConfirmOld,
        pinConfirmNew = this.pinLockTranslations.pinConfirmNew,
        pinConfirmVerify = this.pinLockTranslations.pinConfirmVerify,
        pinConfirmOldIncorrect = this.pinLockTranslations.pinConfirmOldIncorrect,
        pinForgotAlert = this.pinLockTranslations.pinForgotAlert,
        pinForgotButtonText = this.pinLockTranslations.pinForgotButtonText,
        pinForgotButtonCancel = this.pinLockTranslations.pinForgotButtonCancel,
        pinAttemptsText = this.pinLockTranslations.pinAttemptsText
    )

    val popupTranslations = LocalizedPopupTranslations(
        popupExitMessage = this.popupTranslations.popupExitMessage,
        popupYes = this.popupTranslations.popupYes,
        popupNo = this.popupTranslations.popupNo
    )

    return LocalizedModels(
        featureSettings = featureSettings,
        generalMessages = generalMessages,
        errorMessages = errorMessages,
        osVersionSettings = osVersionSettings,
        splashTranslations = splashTranslations,
        tutorialTranslations = tutorialTranslations,
        pinLockTranslations = pinLockTranslations,
        biometricsTranslations = biometricsTranslations,
        popupMessages = popupTranslations
    )
}


@Serializable
data class DefaultLocalizedModels(
    val featureSettings: DefaultLocalizedFeatureSettings = DefaultLocalizedFeatureSettings(),
    val generalMessages: DefaultLocalizedGeneralTranslations = DefaultLocalizedGeneralTranslations(),
    val errorMessages: DefaultLocalizedErrorTranslations = DefaultLocalizedErrorTranslations(),
    val osVersionSettings: DefaultLocalizedOsVersionTranslations = DefaultLocalizedOsVersionTranslations(),
    val splashTranslations: DefaultLocalizedSplashTranslations = DefaultLocalizedSplashTranslations(),
    val tutorialTranslations: DefaultLocalizedTutorialTranslations = DefaultLocalizedTutorialTranslations(),
    val pinLockTranslations: DefaultLocalizedPinLockTranslations = DefaultLocalizedPinLockTranslations(),
    val biometricsTranslations: DefaultLocalizedBiometricsTranslations = DefaultLocalizedBiometricsTranslations(),
    val popupTranslations: DefaultLocalizedPopupTranslations = DefaultLocalizedPopupTranslations()
)

@Serializable
data class DefaultLocalizedOsVersionTranslations(
    val minOsVersionTitle: String = "Tương thích với",
    val minOsVersionMessage: String = "Bạn đang sử dụng điện thoại với hệ điều hành dưới mức yêu cầu tối thiểu. Một số chức năng có thể không được hỗ trợ.",
    val minOsVersionInstruction: String = "Không hiển thị lại",
    val minOsVersionOk: String = "OK"
)

@Serializable
data class DefaultLocalizedFeatureSettings(
    val minOsVersionIsEnabled: Boolean = false,
    val minOsVersionAndroid: Double = 7.1,
    val pinlockEnabled: Boolean = false,
    val biometricsEnabled: Boolean = false
)

@Serializable
data class DefaultLocalizedPopupTranslations(
    val popupExitMessage: String = "Thoát Trang?",
    val popupYes: String = "Có",
    val popupNo: String = "Không"
)

@Serializable
data class DefaultLocalizedGeneralTranslations(
    val retry: String = "Thử Lại",
    val exitApp: String = "Thoát Ứng Dụng",
    val ok: String = "OK",
    val download: String = "Tải Về",
    val openingPage: String = "Đang Mở Trang",
    val maintenanceDialog: String = "Trang đang bảo trì, vui lòng thử lại sau.",
    val geoblockedDialog: String = "Xin lỗi! Dịch vụ không có sẵn trong quốc gia của bạn"
)

@Serializable
data class DefaultLocalizedErrorTranslations(
    val network: String = "(Error Code : N001) Lỗi Kết Nối Mạng",
    val fetchDomain: String = "Lỗi Tải Tên Miền",
    val homepage: String = "Đang mở trang chủ…"
)

@Serializable
data class DefaultLocalizedSplashTranslations(
    val splashSkipButton: String = "Bỏ qua"
)

@Serializable
data class DefaultLocalizedTutorialTranslations(
    val tutorialNextButton: String = "Tiếp tục",
    val tutorialEndButton: String = "Đã hiểu"
)

@Serializable
data class DefaultLocalizedPinLockTranslations(
    val pinLockScreen: String = "Mật khẩu khóa màn hình",
    val pinCreate: String = "Tạo 1 mật khẩu khóa màn hình",
    val pinConfirm: String = "Xác nhận mật khẩu khóa màn hình",
    val pinEnter: String = "Nhập mật khẩu khóa màn hình",
    val pinIncorrect: String = "Mật khẩu không chính xác. Vui lòng nhập lại.",
    val pinIncorrectNew: String = "Mật khẩu khóa màn hình mới không chính xác. Vui lòng nhập lại.",
    val pinConfirmOld: String = "Nhập mật khẩu khóa màn hình cũ",
    val pinConfirmNew: String = "Nhập mật khẩu khóa màn hình mới",
    val pinConfirmVerify: String = "Xác nhận mật khẩu khóa màn hình mới",
    val pinConfirmOldIncorrect: String = "Mật khẩu khóa màn hình cũ không chính xác. Vui lòng nhập lại.",
    val pinForgotAlert: String = "Nếu bạn không nhớ mật khẩu khóa màn hình, vui lòng thay đổi mật khẩu tài khoản. Kiểm tra email sau khi thay đổi mật khẩu thành công.",
    val pinForgotButtonText: String = "Bạn không nhớ mật khẩu khóa màn hình?",
    val pinForgotButtonCancel: String = "Hủy Cài Đặt",
    val pinAttemptsText: String = "Nỗ lực còn lại:",
)

@Serializable
data class DefaultLocalizedBiometricsTranslations(
    val promptInfoTitle: String = "Đăng nhập Sinh trắc học",
    val promptInfoCancel: String = "Hủy bỏ",
    val biometricsSelectAccount: String = "Chọn tài khoản để đăng nhập",
    val biometricsErrorHwUnavailable: String = "Phần cứng không khả dụng",
    val biometricsErrorNoneEnrolled: String = "Không có sinh trắc học nào được đăng ký",
    val biometricsErrorNoHardware: String = "Phần cứng sinh trắc học không tồn tại",
    val biometricsErrorSecurityUpdate: String = "Phát hiện lỗ hổng bảo mật. Cảm biến sẽ không khả dụng cho đến khi có bản cập nhật sửa lỗ hổng này.",
    val biometricsAlertTitle: String = "Xác nhận bật xác thực sinh trắc học?",
    val biometricsAlertMessage: String = "Sử dụng sinh trắc học của bạn trong lần đăng nhập tiếp theo.",
    val biometricsAlertPositiveBtn: String = "BẬT",
    val biometricsAlertNegativeBtn: String = "ĐỂ SAU",
    val biometricsAlertNeutralBtn: String = "KHÔNG HIỂN THỊ LẠI",
    val biometricsToastMessage: String = "Đăng nhập bằng Sinh trắc học đã được bật",
    val biometricsErrorSetupRequired: String = "Đăng nhập bằng sinh trắc học không khả dụng. Vui lòng cài đặt dấu vân tay và đăng nhập lại để khởi tạo tính năng này.",
    val biometricsErrorCanceled: String = "Hủy Bỏ",
    val biometricsErrorDefault: String = "Có thể Sinh trắc học chưa được thiết lập hoặc đã bị thay đổi. Vui lòng đăng nhập lại để kích hoạt tính năng này.",
    val biometricsErrorLockout: String = "Quá nhiều lần thử, vui lòng thử lại sau",
    val biometricsErrorLoginFailed: String = "Đăng nhập thất bại. Vui lòng nhập mật khẩu của bạn"
)