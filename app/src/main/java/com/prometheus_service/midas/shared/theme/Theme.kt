package com.prometheus_service.midas.shared.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    // second stage
    secondaryContainer = Color(0XFF0E0E0E),
    onSecondaryContainer = Color(0XFFF6C244),
    onSecondary = Color(0XFFC5CBD3),

    tertiaryContainer = Color(0XFF0E0E0E),
    onTertiaryContainer = Color.White,
    surface = Color.DarkGray,

    // Language Selection
    background = Color(0XFFF0F2F4),
    onBackground = Color(0XFF424852),

    surfaceVariant = Color(0XFFFFFFFF),
    onSurfaceVariant = Color(0XFF212121)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    tertiary = Pink40,

    // second stage
    secondary = PurpleGrey40,
    secondaryContainer = Color(0XFFF0F2F4),
    onSecondaryContainer = Color(0XFFC4271C),
    onSecondary = Color(0XFF424852),

    tertiaryContainer = Color(0XFFF0F2F4),
    onTertiaryContainer = Color.DarkGray,
    surface = Color.White,

    // Language Selection
    background = Color(0XFFF0F2F4),
    onBackground = Color(0XFF424852),

    surfaceVariant = Color(0XFFFFFFFF),
    onSurfaceVariant = Color(0XFF212121)

    /* Other default colors to override
    background = Color(0xFFFFFBFE),

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MidasAndroidNativeRevampTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}