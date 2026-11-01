package com.shader.app

import android.graphics.RuntimeShader
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ShaderBrush

actual val platformName: String = "Android ${Build.VERSION.RELEASE}"

actual val supportsRuntimeShaders: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

@Composable
actual fun ShaderSurface(
    effect: ShaderEffect,
    time: () -> Float,
    touch: () -> Offset?,
    modifier: Modifier,
) {
    if (supportsRuntimeShaders) {
        AgslSurface(effect, time, touch, modifier)
    } else {
        FallbackSurface(effect, time, touch, modifier)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun AgslSurface(
    effect: ShaderEffect,
    time: () -> Float,
    touch: () -> Offset?,
    modifier: Modifier,
) {
    // Si el shader no compila en este dispositivo, usamos el modo de compatibilidad.
    val shader = remember(effect.id) { runCatching { RuntimeShader(effect.source) }.getOrNull() }
    if (shader == null) {
        FallbackSurface(effect, time, touch, modifier)
        return
    }
    val brush = remember(shader) { ShaderBrush(shader) }
    Canvas(modifier) {
        val t = touch() ?: center
        shader.setFloatUniform("iResolution", size.width, size.height)
        shader.setFloatUniform("iTime", time())
        shader.setFloatUniform("iTouch", t.x, t.y)
        drawRect(brush)
    }
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}
