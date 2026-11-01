package com.shader.app

import androidx.compose.ui.graphics.Color

/**
 * Un efecto visual escrito en el subconjunto común de AGSL (Android 13+) y SkSL
 * (escritorio / iOS a través de Skia). Ambos lenguajes comparten sintaxis, así que
 * el mismo código fuente funciona en todas las plataformas.
 *
 * Uniforms disponibles (los declara [ShaderLibrary.HEADER]):
 *  - iResolution: tamaño del lienzo en píxeles
 *  - iTime: tiempo en segundos
 *  - iTouch: posición del dedo / ratón en píxeles (centro si no hay toque)
 */
data class ShaderEffect(
    val id: String,
    val name: String,
    val description: String,
    val body: String,
    /** Colores usados por el modo de compatibilidad en dispositivos sin AGSL. */
    val palette: List<Color>,
) {
    val source: String get() = ShaderLibrary.HEADER + body
}

object ShaderLibrary {
    const val HEADER = """
uniform float2 iResolution;
uniform float iTime;
uniform float2 iTouch;

float2 centered(float2 fc) {
    return (fc - 0.5 * iResolution) / min(iResolution.x, iResolution.y);
}
"""

    val plasma = ShaderEffect(
        id = "plasma",
        name = "Plasma",
        description = "Ondas de color que se mezclan. Toca para mover el centro de la onda.",
        palette = listOf(Color(0xFFFF4D9D), Color(0xFF6A5BFF), Color(0xFF00E5FF)),
        body = """
half4 main(float2 fc) {
    float2 p = centered(fc);
    float2 m = centered(iTouch);
    float v = sin(p.x * 6.0 + iTime)
            + sin((p.y * 6.0 + iTime) * 0.7)
            + sin(length(p - m) * 10.0 - iTime * 2.0)
            + sin(length(p) * 8.0 + iTime);
    float3 c = 0.5 + 0.5 * cos(1.5708 * v + float3(0.0, 2.0, 4.0) + iTime * 0.3);
    return half4(half3(c), 1.0);
}
""",
    )

    val tunnel = ShaderEffect(
        id = "tunnel",
        name = "Túnel",
        description = "Un viaje infinito por un túnel de neón. Arrastra para mirar alrededor.",
        palette = listOf(Color(0xFF14002E), Color(0xFF00E5FF), Color(0xFFB000FF)),
        body = """
half4 main(float2 fc) {
    float2 p = centered(fc) - centered(iTouch) * 0.6;
    float r = length(p) + 0.0001;
    float a = atan(p.y, p.x);
    float u = 0.35 / r + iTime * 0.9;
    float v = a / 3.14159 * 4.0 + iTime * 0.15;
    float cu = smoothstep(0.8, 0.95, abs(fract(u) - 0.5) * 2.0);
    float cv = smoothstep(0.8, 0.95, abs(fract(v) - 0.5) * 2.0);
    float grid = max(cu, cv);
    float3 base = 0.5 + 0.5 * cos(u * 0.6 + float3(0.0, 1.5, 3.0));
    float3 col = mix(base * 0.12, base, grid);
    col *= clamp(r * 2.2, 0.0, 1.0);
    return half4(half3(col), 1.0);
}
""",
    )

    val aurora = ShaderEffect(
        id = "aurora",
        name = "Aurora",
        description = "Cortinas de luz boreal ondulando en el cielo nocturno.",
        palette = listOf(Color(0xFF020617), Color(0xFF22FFAA), Color(0xFF8A5CFF)),
        body = """
half4 main(float2 fc) {
    float2 uv = fc / iResolution;
    float2 m = iTouch / iResolution;
    float3 col = mix(float3(0.06, 0.08, 0.2), float3(0.0, 0.01, 0.04), uv.y);
    for (int i = 0; i < 5; i++) {
        float fi = float(i);
        float y = 0.35 + fi * 0.07 + (m.y - 0.5) * 0.3
                + 0.12 * sin(uv.x * 3.0 + iTime * (0.4 + fi * 0.15) + fi * 1.7)
                + 0.04 * sin(uv.x * 11.0 - iTime * 1.3 + fi);
        float d = abs(uv.y - y);
        float3 tint = mix(float3(0.1, 1.0, 0.6), float3(0.6, 0.3, 1.0), fi / 4.0);
        col += tint * 0.006 / (d + 0.008);
    }
    float stars = step(0.997, fract(sin(dot(floor(fc / 2.0), float2(12.9898, 78.233))) * 43758.5453));
    col += stars * (1.0 - uv.y) * 0.8;
    return half4(half3(clamp(col, 0.0, 1.0)), 1.0);
}
""",
    )

    val metaballs = ShaderEffect(
        id = "metaballs",
        name = "Metabolas",
        description = "Gotas líquidas que se fusionan. Tu dedo es una gota más.",
        palette = listOf(Color(0xFF0B1026), Color(0xFFFF7A00), Color(0xFFFF2E63)),
        body = """
half4 main(float2 fc) {
    float2 p = centered(fc);
    float2 m = centered(iTouch);
    float s = 0.0;
    for (int i = 0; i < 6; i++) {
        float fi = float(i);
        float2 c = 0.38 * float2(sin(iTime * (0.5 + 0.13 * fi) + fi * 2.1),
                                 cos(iTime * (0.4 + 0.17 * fi) + fi * 1.3));
        float2 d = p - c;
        s += 0.012 / dot(d, d);
    }
    float2 dm = p - m;
    s += 0.018 / dot(dm, dm);
    s = min(s, 6.0);
    float3 inside = 0.5 + 0.5 * cos(float3(0.0, 0.8, 1.6) + s * 0.6 + iTime * 0.5);
    float edge = smoothstep(0.95, 1.05, s);
    float3 col = mix(float3(0.03, 0.04, 0.1), inside, edge);
    col += float3(1.0, 0.4, 0.2) * clamp(s * 0.12, 0.0, 0.5) * (1.0 - edge);
    return half4(half3(col), 1.0);
}
""",
    )

    val julia = ShaderEffect(
        id = "julia",
        name = "Fractal",
        description = "Conjunto de Julia. Mueve el dedo para transformar el fractal.",
        palette = listOf(Color(0xFF000814), Color(0xFFFFC300), Color(0xFF003566)),
        body = """
half4 main(float2 fc) {
    float2 z = centered(fc) * 2.6;
    float2 m = centered(iTouch);
    float2 c = float2(-0.79, 0.15) + m * 0.25
             + 0.04 * float2(cos(iTime * 0.3), sin(iTime * 0.37));
    float n = 0.0;
    for (int i = 0; i < 80; i++) {
        z = float2(z.x * z.x - z.y * z.y, 2.0 * z.x * z.y) + c;
        if (dot(z, z) > 16.0) { break; }
        n += 1.0;
    }
    float3 col = float3(0.0);
    if (n < 80.0) {
        float sn = n - log2(log2(dot(z, z))) + 4.0;
        col = 0.5 + 0.5 * cos(3.0 + sn * 0.15 + float3(0.0, 0.6, 1.0));
    }
    return half4(half3(col), 1.0);
}
""",
    )

    val fire = ShaderEffect(
        id = "fire",
        name = "Fuego",
        description = "Llamas procedurales generadas con ruido fractal.",
        palette = listOf(Color(0xFF1A0000), Color(0xFFFF5400), Color(0xFFFFD000)),
        body = """
float hash(float2 p) {
    return fract(sin(dot(p, float2(127.1, 311.7))) * 43758.5453);
}

float noise(float2 p) {
    float2 i = floor(p);
    float2 f = fract(p);
    float2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(hash(i), hash(i + float2(1.0, 0.0)), u.x),
               mix(hash(i + float2(0.0, 1.0)), hash(i + float2(1.0, 1.0)), u.x), u.y);
}

float fbm(float2 p) {
    float v = 0.0;
    float a = 0.5;
    for (int i = 0; i < 5; i++) {
        v += a * noise(p);
        p *= 2.0;
        a *= 0.5;
    }
    return v;
}

half4 main(float2 fc) {
    float2 uv = fc / iResolution;
    float h = 1.0 - uv.y;
    float2 m = iTouch / iResolution;
    float x = (uv.x - m.x) * 3.0;
    float2 q = float2(x, h * 2.0 - iTime * 1.6);
    float n = fbm(q + fbm(q * 1.5 + iTime * 0.3));
    float flame = n * 1.9 - h * 1.2 - abs(x) * 0.25;
    flame = clamp(flame, 0.0, 1.0);
    float3 col = float3(1.5 * flame, 1.5 * flame * flame * flame, flame * flame * flame * flame * flame * flame);
    return half4(half3(clamp(col, 0.0, 1.0)), 1.0);
}
""",
    )

    val all = listOf(plasma, tunnel, aurora, metaballs, julia, fire)
}
