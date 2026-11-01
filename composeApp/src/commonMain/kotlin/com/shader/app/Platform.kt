package com.shader.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

/** Nombre de la plataforma actual (se muestra en la pantalla principal). */
expect val platformName: String

/** true si la plataforma puede ejecutar shaders en tiempo real (AGSL / SkSL). */
expect val supportsRuntimeShaders: Boolean

/**
 * Dibuja [effect] ocupando todo el espacio de [modifier].
 * [time] y [touch] se leen en la fase de dibujo para no recomponer en cada frame.
 */
@Composable
expect fun ShaderSurface(
    effect: ShaderEffect,
    time: () -> Float,
    touch: () -> Offset?,
    modifier: Modifier = Modifier,
)

/** Botón "atrás" del sistema (solo tiene efecto en Android). */
@Composable
expect fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit)
