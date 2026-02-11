package com.prometheus_service.midas.core.presentation.main_screen.presentation.util

class Constants {
    companion object Companion {
        val LOGIN_LAUNCHER_SCRIPT = "javascript: Android.loginLauncher('')"
        val HIDE_BIOMETRICS_SCRIPT =
            "javascript: window.app._events['native-biometrics-is-enabled'][0](false)"
        val DISPLAY_BIOMETRICS_SCRIPT =
            "javascript: window.app._events['native-biometrics-is-enabled'][0](true)"
        val LOGIN_ROUTE = "javascript: window.pwa.navigate({ name: 'login-route'})"

        fun authenticateScript(memberCode: String, password: String): String {
            return "javascript: window.pwa.authenticate({" +
                    "username:'$memberCode'," +
                    "password:'$password'" +
                    "});"
        }

    }
}