package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldAccent,
    onPrimary = Color.Black,
    primaryContainer = EmeraldDark,
    onPrimaryContainer = SlateTextPrimary,
    secondary = EmeraldLight,
    onSecondary = Color.Black,
    background = PlatinumBgDark,
    onBackground = SlateTextPrimary,
    surface = PlatinumSurfaceDark,
    onSurface = SlateTextPrimary,
    surfaceVariant = PlatinumSurfaceElevated,
    onSurfaceVariant = SlateTextSecondary,
    outline = SlateBorder,
    error = SignalError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldDark,
    onPrimary = Color.White,
    primaryContainer = EmeraldLight,
    onPrimaryContainer = Color.Black,
    secondary = EmeraldAccent,
    onSecondary = Color.White,
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF16171B),
    surface = Color.White,
    onSurface = Color(0xFF16171B),
    surfaceVariant = Color(0xFFE9ECEF),
    onSurfaceVariant = Color(0xFF495057),
    outline = Color(0xFFCED4DA),
    error = SignalError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force premium dark by default for the government-grade visual depth
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
