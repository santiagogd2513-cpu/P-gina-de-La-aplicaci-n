package com.lunamistica.app

import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder
import org.jetbrains.skia.Surface
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Compila cada shader con Skia (SkSL, misma sintaxis que AGSL) y guarda una vista previa
 * en build/shader-previews/ para revisarla visualmente.
 */
class ShaderCompileTest {
    @Test
    fun allShadersCompileAndRender() {
        val outDir = File("build/shader-previews").apply { mkdirs() }
        val w = 360
        val h = 640
        for (effect in ShaderLibrary.all) {
            val builder = RuntimeShaderBuilder(RuntimeEffect.makeForShader(effect.source))
            builder.uniform("iResolution", w.toFloat(), h.toFloat())
            builder.uniform("iTime", 3.5f)
            builder.uniform("iTouch", w * 0.5f, h * 0.5f)
            val surface = Surface.makeRasterN32Premul(w, h)
            surface.canvas.drawRect(Rect.makeWH(w.toFloat(), h.toFloat()), Paint().apply { shader = builder.makeShader() })
            val png = surface.makeImageSnapshot().encodeToData(EncodedImageFormat.PNG)!!.bytes
            File(outDir, "${effect.id}.png").writeBytes(png)
            assertTrue(png.isNotEmpty(), "Sin salida para ${effect.name}")
        }
    }
}
