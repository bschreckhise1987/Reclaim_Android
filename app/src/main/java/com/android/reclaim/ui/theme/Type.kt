package com.android.reclaim.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val DefaultTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

fun getTypography(largeText: Boolean): Typography {
    if (!largeText) return DefaultTypography

    val scale = 1.25f
    return Typography(
        displayLarge = DefaultTypography.displayLarge.copy(
            fontSize = DefaultTypography.displayLarge.fontSize * scale,
            lineHeight = DefaultTypography.displayLarge.lineHeight * scale
        ),
        displayMedium = DefaultTypography.displayMedium.copy(
            fontSize = DefaultTypography.displayMedium.fontSize * scale,
            lineHeight = DefaultTypography.displayMedium.lineHeight * scale
        ),
        displaySmall = DefaultTypography.displaySmall.copy(
            fontSize = DefaultTypography.displaySmall.fontSize * scale,
            lineHeight = DefaultTypography.displaySmall.lineHeight * scale
        ),
        headlineLarge = DefaultTypography.headlineLarge.copy(
            fontSize = DefaultTypography.headlineLarge.fontSize * scale,
            lineHeight = DefaultTypography.headlineLarge.lineHeight * scale
        ),
        headlineMedium = DefaultTypography.headlineMedium.copy(
            fontSize = DefaultTypography.headlineMedium.fontSize * scale,
            lineHeight = DefaultTypography.headlineMedium.lineHeight * scale
        ),
        headlineSmall = DefaultTypography.headlineSmall.copy(
            fontSize = DefaultTypography.headlineSmall.fontSize * scale,
            lineHeight = DefaultTypography.headlineSmall.lineHeight * scale
        ),
        titleLarge = DefaultTypography.titleLarge.copy(
            fontSize = DefaultTypography.titleLarge.fontSize * scale,
            lineHeight = DefaultTypography.titleLarge.lineHeight * scale
        ),
        titleMedium = DefaultTypography.titleMedium.copy(
            fontSize = DefaultTypography.titleMedium.fontSize * scale,
            lineHeight = DefaultTypography.titleMedium.lineHeight * scale
        ),
        titleSmall = DefaultTypography.titleSmall.copy(
            fontSize = DefaultTypography.titleSmall.fontSize * scale,
            lineHeight = DefaultTypography.titleSmall.lineHeight * scale
        ),
        bodyLarge = DefaultTypography.bodyLarge.copy(
            fontSize = DefaultTypography.bodyLarge.fontSize * scale,
            lineHeight = DefaultTypography.bodyLarge.lineHeight * scale
        ),
        bodyMedium = DefaultTypography.bodyMedium.copy(
            fontSize = DefaultTypography.bodyMedium.fontSize * scale,
            lineHeight = DefaultTypography.bodyMedium.lineHeight * scale
        ),
        bodySmall = DefaultTypography.bodySmall.copy(
            fontSize = DefaultTypography.bodySmall.fontSize * scale,
            lineHeight = DefaultTypography.bodySmall.lineHeight * scale
        ),
        labelLarge = DefaultTypography.labelLarge.copy(
            fontSize = DefaultTypography.labelLarge.fontSize * scale,
            lineHeight = DefaultTypography.labelLarge.lineHeight * scale
        ),
        labelMedium = DefaultTypography.labelMedium.copy(
            fontSize = DefaultTypography.labelMedium.fontSize * scale,
            lineHeight = DefaultTypography.labelMedium.lineHeight * scale
        ),
        labelSmall = DefaultTypography.labelSmall.copy(
            fontSize = DefaultTypography.labelSmall.fontSize * scale,
            lineHeight = DefaultTypography.labelSmall.lineHeight * scale
        )
    )
}
