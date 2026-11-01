package com.lunamistica.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6B4EA2),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEBDDFF),
    onPrimaryContainer = Color(0xFF25005A),
    secondary = Color(0xFF8A6A1F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE3A3),
    onSecondaryContainer = Color(0xFF2B1F00),
    tertiary = Color(0xFF9C4372),
    background = Color(0xFFFBF8FF),
    onBackground = Color(0xFF1C1B22),
    surface = Color(0xFFFBF8FF),
    onSurface = Color(0xFF1C1B22),
    surfaceVariant = Color(0xFFE8E0F0),
    onSurfaceVariant = Color(0xFF4A4553),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF6F1FC),
    surfaceContainer = Color(0xFFF1EBF8),
    surfaceContainerHigh = Color(0xFFEBE5F3),
    surfaceContainerHighest = Color(0xFFE5DFED),
    outline = Color(0xFF7B7484),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD3BFFF),
    onPrimary = Color(0xFF3A1E72),
    primaryContainer = Color(0xFF52388C),
    onPrimaryContainer = Color(0xFFEBDDFF),
    secondary = Color(0xFFF2C66D),
    onSecondary = Color(0xFF3F2E00),
    secondaryContainer = Color(0xFF5B4300),
    onSecondaryContainer = Color(0xFFFFE3A3),
    tertiary = Color(0xFFFFAFD5),
    background = Color(0xFF13111A),
    onBackground = Color(0xFFE7E1EC),
    surface = Color(0xFF13111A),
    onSurface = Color(0xFFE7E1EC),
    surfaceVariant = Color(0xFF4A4553),
    onSurfaceVariant = Color(0xFFCCC4D6),
    surfaceContainerLowest = Color(0xFF0E0C14),
    surfaceContainerLow = Color(0xFF1B1823),
    surfaceContainer = Color(0xFF201C28),
    surfaceContainerHigh = Color(0xFF2A2633),
    surfaceContainerHighest = Color(0xFF35303E),
    outline = Color(0xFF958E9F),
)

/** Tema de la app: sigue automáticamente el modo claro/oscuro del sistema. */
@Composable
fun LunaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}
