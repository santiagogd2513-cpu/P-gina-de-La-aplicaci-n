@file:OptIn(ExperimentalMaterial3Api::class)

package com.lunamistica.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Una de las secciones principales de la app. */
data class Category(
    val title: String,
    val emoji: String,
    val subtitle: String,
    val effect: ShaderEffect,
    val screen: Screen,
)

object Categories {
    val tarot = Category("Tarot", "🔮", "Tiradas y significados de los Arcanos Mayores", ShaderLibrary.cosmos, Screen.Tarot)
    val pendulum = Category("Péndulo", "💎", "Respuestas sí o no y alineación de chakras", ShaderLibrary.tunnel, Screen.Pendulum)
    val baths = Category("Baños", "🛁", "Baños rituales con hierbas, flores y sal", ShaderLibrary.metaballs, Screen.Baths)
    val prayers = Category("Rezos", "🙏", "Oraciones para cada intención", ShaderLibrary.aurora, Screen.Prayers)
    val cleansings = Category("Limpiezas", "🌿", "Limpiezas energéticas para ti y tu hogar", ShaderLibrary.fire, Screen.Cleansings)
    val numerology = Category("Numerología", "🔢", "Descubre los números de tu nombre y tu fecha", ShaderLibrary.julia, Screen.Numerology)

    val all = listOf(tarot, pendulum, baths, prayers, cleansings, numerology)
}

/** Cabecera con un shader animado de fondo, botón atrás y título. */
@Composable
fun BannerHeader(
    effect: ShaderEffect,
    emoji: String,
    title: String,
    subtitle: String,
    onBack: () -> Unit,
) {
    val time by rememberShaderTime()
    Box(Modifier.fillMaxWidth().height(220.dp)) {
        ShaderSurface(effect, { time }, { null }, Modifier.matchParentSize())
        Box(
            Modifier.matchParentSize().background(
                Brush.verticalGradient(
                    listOf(Color.Black.copy(alpha = 0.35f), Color.Black.copy(alpha = 0.05f), Color.Black.copy(alpha = 0.7f))
                )
            )
        )
        FilledTonalIconButton(
            onClick = onBack,
            modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars).padding(12.dp),
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
        Column(Modifier.align(Alignment.BottomStart).padding(20.dp)) {
            Text(emoji, fontSize = 34.sp)
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
        }
    }
}

@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text,
        modifier = modifier.padding(top = 20.dp, bottom = 8.dp),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
    )
}

/** Tarjeta de lista con emoji, título y descripción. */
@Composable
fun EmojiListItem(emoji: String, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(52.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) { Text(emoji, fontSize = 26.sp) }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/** Lista con viñetas. */
@Composable
fun BulletList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEach { item ->
            Row {
                Text("✦", color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(end = 10.dp))
                Text(item, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/** Fila clicable sin efecto visual especial (para checklists). */
fun Modifier.plainClickable(onClick: () -> Unit) = this.clickable(onClick = onClick)
