package com.shader.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

private val ShaderColors = darkColorScheme(
    primary = Color(0xFF9D8CFF),
    secondary = Color(0xFF4DE1FF),
    background = Color(0xFF0B0B14),
    surface = Color(0xFF15151F),
)

@Composable
fun App() {
    MaterialTheme(colorScheme = ShaderColors) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            var selected by remember { mutableStateOf<ShaderEffect?>(null) }
            val current = selected
            if (current == null) {
                GalleryScreen(onOpen = { selected = it })
            } else {
                PlatformBackHandler(enabled = true) { selected = null }
                DetailScreen(current, onBack = { selected = null })
            }
        }
    }
}

@Composable
private fun GalleryScreen(onOpen: (ShaderEffect) -> Unit) {
    val time by rememberShaderTime()
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(Modifier.padding(bottom = 8.dp)) {
                Text("Shader", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                Text(
                    "Galería de efectos en tiempo real · $platformName",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (!supportsRuntimeShaders) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Tu dispositivo no soporta shaders AGSL (requiere Android 13+). " +
                            "Se muestra una versión simplificada.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
        }
        items(ShaderLibrary.all, key = { it.id }) { effect ->
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.clickable { onOpen(effect) },
            ) {
                ShaderSurface(
                    effect = effect,
                    time = { time },
                    touch = { null },
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(20.dp)),
                )
                Text(
                    effect.name,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Composable
private fun DetailScreen(effect: ShaderEffect, onBack: () -> Unit) {
    var speed by remember { mutableFloatStateOf(1f) }
    var paused by remember { mutableStateOf(false) }
    var touch by remember { mutableStateOf<Offset?>(null) }
    val time by rememberShaderTime(speed = speed, paused = paused)

    Box(Modifier.fillMaxSize()) {
        ShaderSurface(
            effect = effect,
            time = { time },
            touch = { touch },
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures { touch = it } }
                .pointerInput(Unit) {
                    detectDragGestures(onDragStart = { touch = it }) { change, _ ->
                        touch = change.position
                    }
                },
        )

        Column(
            Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledTonalIconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    effect.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }

            Spacer(Modifier.weight(1f))

            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.Black.copy(alpha = 0.55f))
                    .padding(16.dp),
            ) {
                Text(effect.description, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))
                Text(
                    "Velocidad: ${(speed * 10).roundToInt() / 10f}x",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.labelLarge,
                )
                Slider(value = speed, onValueChange = { speed = it }, valueRange = 0.1f..3f)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FilledTonalButton(onClick = { paused = !paused }) {
                        if (paused) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Reanudar")
                        } else {
                            Text("❚❚  Pausar")
                        }
                    }
                    FilledTonalButton(onClick = { touch = null; speed = 1f }) {
                        Icon(Icons.Filled.Refresh, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Reiniciar")
                    }
                }
            }
        }
    }
}
