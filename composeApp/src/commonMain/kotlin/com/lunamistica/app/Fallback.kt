package com.lunamistica.app

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import kotlin.math.cos
import kotlin.math.sin

/** Reloj animado: avanza [speed] segundos por segundo real mientras no esté en pausa. */
@Composable
fun rememberShaderTime(speed: Float = 1f, paused: Boolean = false): State<Float> {
    val time = remember { mutableFloatStateOf(0f) }
    val currentSpeed = rememberUpdatedState(speed)
    LaunchedEffect(paused) {
        if (paused) return@LaunchedEffect
        var last = withFrameNanos { it }
        while (true) {
            withFrameNanos { now ->
                time.floatValue += (now - last) / 1_000_000_000f * currentSpeed.value
                last = now
            }
        }
    }
    return time
}

/**
 * Versión simplificada (sin shaders) para dispositivos Android anteriores a la versión 13,
 * que no soportan AGSL. Usa gradientes y círculos animados con la paleta del efecto.
 */
@Composable
fun FallbackSurface(
    effect: ShaderEffect,
    time: () -> Float,
    touch: () -> Offset?,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier) {
        val t = time()
        val colors = effect.palette
        val shift = sin(t * 0.7f) * 0.5f + 0.5f
        drawRect(
            Brush.linearGradient(
                colors = colors,
                start = Offset(size.width * shift, 0f),
                end = Offset(size.width * (1f - shift), size.height),
            )
        )
        repeat(4) { i ->
            val center = Offset(
                size.width * (0.5f + 0.35f * cos(t * (0.5f + 0.2f * i) + i)),
                size.height * (0.5f + 0.35f * sin(t * (0.6f + 0.15f * i) + i * 2)),
            )
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(colors[(i + 1) % colors.size].copy(alpha = 0.7f), colors[0].copy(alpha = 0f)),
                    center = center,
                    radius = size.minDimension * (0.25f + 0.05f * i),
                ),
                radius = size.minDimension * (0.25f + 0.05f * i),
                center = center,
            )
        }
        touch()?.let { p ->
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(colors.last().copy(alpha = 0.9f), colors.last().copy(alpha = 0f)),
                    center = p,
                    radius = size.minDimension * 0.2f,
                ),
                radius = size.minDimension * 0.2f,
                center = p,
            )
        }
    }
}
