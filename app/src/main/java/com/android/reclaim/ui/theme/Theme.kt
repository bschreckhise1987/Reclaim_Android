package com.android.reclaim.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFFE5E5EA)
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    surfaceVariant = Color(0xFFF2F2F7),
    onSurfaceVariant = Color(0xFF3A3A3C)
)

private val HighContrastDarkColorScheme = darkColorScheme(
    primary = Color(0xFF66B2FF),
    onPrimary = Color.Black,
    surface = Color.Black,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1F1F1F),
    onSurfaceVariant = Color.White,
    background = Color.Black,
    onBackground = Color.White
)

private val HighContrastLightColorScheme = lightColorScheme(
    primary = Color(0xFF004080),
    onPrimary = Color.White,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color.Black,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun ReclaimTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    largeText: Boolean = false,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        highContrast -> if (darkTheme) HighContrastDarkColorScheme else HighContrastLightColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val typography = remember(largeText) {
        getTypography(largeText)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
