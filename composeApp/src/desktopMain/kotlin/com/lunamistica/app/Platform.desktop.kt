package com.lunamistica.app

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ShaderBrush
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder

actual val platformName: String = "Escritorio (${System.getProperty("os.name")})"

actual val supportsRuntimeShaders: Boolean = true

@Composable
actual fun ShaderSurface(
    effect: ShaderEffect,
    time: () -> Float,
    touch: () -> Offset?,
    modifier: Modifier,
) {
    // SkSL (Skia) comparte sintaxis con AGSL, así que reutilizamos el mismo código.
    val builder = remember(effect.id) {
        runCatching { RuntimeShaderBuilder(RuntimeEffect.makeForShader(effect.source)) }.getOrNull()
    }
    if (builder == null) {
        FallbackSurface(effect, time, touch, modifier)
        return
    }
    Canvas(modifier) {
        val t = touch() ?: center
        builder.uniform("iResolution", size.width, size.height)
        builder.uniform("iTime", time())
        builder.uniform("iTouch", t.x, t.y)
        drawRect(ShaderBrush(builder.makeShader()))
    }
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // Sin botón "atrás" del sistema en esta plataforma.
}
