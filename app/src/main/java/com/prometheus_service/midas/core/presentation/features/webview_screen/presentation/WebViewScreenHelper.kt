package com.prometheus_service.midas.core.presentation.features.webview_screen.presentation

import android.app.DownloadManager
import android.os.Environment
import android.webkit.WebView
import androidx.core.net.toUri
import com.prometheus_service.midas.core.presentation.features.webview_screen.javascript.DefaultJavascriptListener
import com.prometheus_service.midas.core.presentation.features.webview_screen.javascript.JavascriptListener
import timber.log.Timber

fun getApkFilename(downloadUrl: String?): String? {
    return downloadUrl
        ?.takeIf { it.contains(".apk", ignoreCase = true) }
        ?.substringBefore("?")
        ?.substringAfterLast("/")
}


fun webviewDownloadInitializer(
    view: WebView,
    downloadManager: DownloadManager,
    onDownloadProcessed: (String) -> Unit,
) {
    view.setDownloadListener { url, _, _, mimetype, _ ->
        val filename = getApkFilename(url)
        if (!url.isNullOrEmpty() && !filename.isNullOrEmpty()) {
            try {
                val request = DownloadManager.Request(url.toUri())
                request.setMimeType(mimetype)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
                downloadManager.enqueue(request)
                Timber.d("Downloading apk file ...")
                onDownloadProcessed(filename)
            } catch (e: Exception) {
                Timber.e(e, "Error while downloading apk file")
            }
        }
    }
}


fun webviewJavascriptSetup(
    webView: WebView,
    onPwaReady: (data: String) -> Unit,
    onNewGameLauncher: (url: String) -> Unit,
    onNativeAuthenticateGoogle: (data: String) -> Unit,
    onNativeLaunchGoogle: (url: String) -> Unit,
    onStoreCredentials: (data: String?) -> Unit,
    onPwaNavigate: (route: String?) -> Unit,
    onShouldDisplayBiometricsLogin: () -> Unit,
    onLoginLauncher: (data: String?) -> Unit,
    onPincodeToggled: (isEnabled: Boolean) -> Unit,
    onMemberLoggedIn: (data: String) -> Unit,
    onMemberLoggedOut: (data: String) -> Unit,
    onSwitchLanguage: (language: String) -> Unit,
    onOpenInBrowser: (url: String) -> Unit,
    onLaunchNewWindow: (url: String) -> Unit,
    onMaintenanceMode: (data: String?) -> Unit,
    onGeoBlockMode: (data: String?) -> Unit,
    onRefreshCookie: (data: String) -> Unit
) {
    webView.addJavascriptInterface(
        DefaultJavascriptListener(
            object : JavascriptListener {
                override fun onPwaReady(data: String) {
                    onPwaReady(data)
                }

                override fun onNewGameLauncher(url: String) {
                    onNewGameLauncher(url)
                }

                override fun onPwaNavigate(route: String?) {
                    onPwaNavigate(route)
                }

                override fun onNativeAuthenticateGoogle(data: String) {
                    onNativeAuthenticateGoogle(data)
                }

                override fun onNativeLaunchGoogle(url: String) {
                    onNativeLaunchGoogle(url)
                }

                override fun onStoreCredentials(data: String?) {
                    onStoreCredentials(data)
                }

                override fun onResetCredentials(data: String?) {
                    //
                }

                override fun onShouldDisplayBiometricsLogin(enabled: Boolean) {
                    onShouldDisplayBiometricsLogin()
                }

                override fun onLoginLauncher(data: String?) {
                    onLoginLauncher(data)
                }

                override fun onPinCodeToggle(isEnabled: Boolean) {
                    onPincodeToggled(isEnabled)
                }

                override fun onMemberLoggedIn(data: String) {
                    onMemberLoggedIn(data)
                }

                override fun onMemberLoggedOut(data: String) {
                    onMemberLoggedOut(data)
                }

                override fun onSwitchLanguage(language: String) {
                    onSwitchLanguage(language)
                }

                override fun onOpenInBrowser(url: String) {
                    onOpenInBrowser(url)
                }

                override fun onLaunchNewWindow(url: String) {
                    onLaunchNewWindow(url)
                }

                override fun onMaintenanceMode(data: String?) {
                    onMaintenanceMode(data)
                }

                override fun onGeoBlockMode(data: String?) {
                    onGeoBlockMode(data)
                }

                override fun onRefreshCookie(data: String) {
                    onRefreshCookie(data)
                }
            }
        ),
        "Android"
    )
}