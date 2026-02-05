package com.fwrdgrp.financetracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MainColorScheme = lightColorScheme(
    primary = AlmostBlack,
    onPrimary = OffWhite,
    primaryContainer = LightTan,
    onPrimaryContainer = DarkestBrown,

    secondary = MediumBrown,
    onSecondary = WarmCream,
    secondaryContainer = LightBrown,
    onSecondaryContainer = DarkestBrown,

    tertiary = SageGreen,
    onTertiary = DarkestBrown,
    tertiaryContainer = LightTan,
    onTertiaryContainer = DarkBrown,

    error = Terracotta,
    onError = WarmCream,
    errorContainer = LightTan,
    onErrorContainer = DarkBrown,

    background = WarmCream,
    onBackground = DarkBrown,

    surface = WarmCream,
    onSurface = DarkBrown,
    surfaceVariant = LightTan,
    onSurfaceVariant = MediumBrown,

    outline = LightBrown,
    outlineVariant = LightTan,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun FinanceTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = MainColorScheme,
        typography = Typography,
        content = content
    )
}