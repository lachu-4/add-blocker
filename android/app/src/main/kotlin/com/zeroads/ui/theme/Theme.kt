package com.zeroads.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberCyan = Color(0xFF00B8FF)
private val CyberBlue = Color(0xFF0070FF)
private val CyberDark = Color(0xFF0A0A0A)
private val CyberSurface = Color(0xFF121212)

private val DarkColorScheme = darkColorScheme(
    primary = CyberCyan,
    secondary = CyberBlue,
    background = CyberDark,
    surface = CyberSurface,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun ZeroAdsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
