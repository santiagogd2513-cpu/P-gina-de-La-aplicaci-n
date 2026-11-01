package com.lunamistica.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

// ---------- Baños y limpiezas ----------

@Composable
fun RitualListScreen(
    category: Category,
    rituals: List<Ritual>,
    onBack: () -> Unit,
    navigate: (Screen) -> Unit,
) {
    // El orden de la lista cambia cada vez que se abre.
    val shuffledRituals = remember(rituals) { rituals.shuffled() }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item { BannerHeader(category.effect, category.emoji, category.title, category.subtitle, onBack) }
        item {
            val moon = remember { Moon.phase() }
            Text(
                "${moon.emoji} Hoy hay ${moon.name.lowercase()}. ${remember { moon.randomAdvice() }}",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(shuffledRituals, key = { it.id }) { ritual ->
            Box(Modifier.padding(horizontal = 16.dp)) {
                EmojiListItem(ritual.emoji, ritual.title, ritual.purpose) {
                    navigate(Screen.RitualDetail(ritual, category.effect))
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
fun RitualDetailScreen(
    ritual: Ritual,
    effect: ShaderEffect,
    onBack: () -> Unit,
    navigate: (Screen) -> Unit,
) {
    val checked = remember(ritual.id) { mutableStateListOf<Int>() }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
    ) {
        item { BannerHeader(effect, ritual.emoji, ritual.title, ritual.purpose, onBack) }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                SectionTitle("🗓️ Cuándo hacerlo")
                Text(ritual.timing, style = MaterialTheme.typography.bodyLarge)
                SectionTitle("🧺 Necesitas (${checked.size}/${ritual.materials.size})")
            }
        }
        itemsIndexed(ritual.materials) { i, material ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .plainClickable { if (i in checked) checked.remove(i) else checked.add(i) }
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = i in checked, onCheckedChange = { if (it) checked.add(i) else checked.remove(i) })
                Text(material, style = MaterialTheme.typography.bodyLarge)
            }
        }
        item { SectionTitle("✨ Paso a paso", Modifier.padding(horizontal = 16.dp)) }
        itemsIndexed(ritual.steps) { i, step ->
            Row(Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                Box(
                    Modifier.size(28.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("${i + 1}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(Modifier.width(12.dp))
                Text(step, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            }
        }
        if (ritual.tips.isNotEmpty()) {
            item {
                Column(Modifier.padding(horizontal = 16.dp)) {
                    SectionTitle("💡 Consejos")
                    BulletList(ritual.tips)
                }
            }
        }
        item {
            Button(
                onClick = { navigate(Screen.Guided(ritual, effect)) },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            ) { Text("Comenzar en modo guiado") }
        }
    }
}

/** Ritual a pantalla completa, paso a paso, sobre un fondo animado. */
@Composable
fun GuidedRitualScreen(ritual: Ritual, effect: ShaderEffect, onBack: () -> Unit) {
    val time by rememberShaderTime(speed = 0.5f)
    var step by remember { mutableIntStateOf(0) }
    val total = ritual.steps.size
    val finished = step >= total
    val doneTitle = remember { Variety.ritualDoneTitles.random() }
    val doneText = remember { Variety.ritualDone.random() }

    Box(Modifier.fillMaxSize()) {
        ShaderSurface(effect, { time }, { null }, Modifier.matchParentSize())
        Box(Modifier.matchParentSize().background(Color.Black.copy(alpha = 0.5f)))
        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing).padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                FilledTonalIconButton(onClick = onBack) { Icon(Icons.Filled.Close, contentDescription = "Cerrar") }
                Spacer(Modifier.width(12.dp))
                Text(ritual.title, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { (step.coerceAtMost(total)).toFloat() / total },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.weight(1f))
            AnimatedContent(targetState = step, label = "step") { s ->
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (s >= total) {
                        Text("✨", fontSize = 56.sp)
                        Text(
                            doneTitle,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            doneText,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    } else {
                        Text(
                            "Paso ${s + 1} de $total",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            ritual.steps[s],
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                if (finished) {
                    Button(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Volver") }
                } else {
                    OutlinedButton(
                        onClick = { step-- },
                        enabled = step > 0,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    ) { Text("Anterior") }
                    Button(onClick = { step++ }, modifier = Modifier.weight(1f)) {
                        Text(if (step == total - 1) "Terminar" else "Siguiente")
                    }
                }
            }
        }
    }
}

// ---------- Rezos ----------

@Composable
fun PrayerListScreen(onBack: () -> Unit, navigate: (Screen) -> Unit) {
    val category = Categories.prayers
    val shuffledPrayers = remember { Prayers.all.shuffled() }
    val prayerIntro = remember { Variety.prayerIntros.random() }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item { BannerHeader(category.effect, category.emoji, category.title, category.subtitle, onBack) }
        item {
            Text(
                prayerIntro,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(shuffledPrayers, key = { it.id }) { prayer ->
            Box(Modifier.padding(horizontal = 16.dp)) {
                EmojiListItem(prayer.emoji, prayer.title, prayer.intention) { navigate(Screen.PrayerDetail(prayer)) }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
fun PrayerDetailScreen(prayer: Prayer, onBack: () -> Unit) {
    var lit by remember { mutableStateOf(false) }
    var count by remember { mutableIntStateOf(0) }
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
    ) {
        item { BannerHeader(Categories.prayers.effect, prayer.emoji, prayer.title, prayer.intention, onBack) }
        item {
            Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Candle(lit)
                Spacer(Modifier.height(8.dp))
                FilledTonalButton(onClick = { lit = !lit }) {
                    Text(if (lit) "Apagar la vela" else "🕯️ Encender la vela")
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    prayer.text,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    "Veces rezada: $count",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { count++ }) { Text("Amén 🙏") }
                    OutlinedButton(onClick = { count = 0 }, enabled = count > 0) { Text("Reiniciar") }
                }
            }
        }
    }
}

/** Vela dibujada con una llama que parpadea cuando está encendida. */
@Composable
private fun Candle(lit: Boolean) {
    val time by rememberShaderTime(paused = !lit)
    val wax = MaterialTheme.colorScheme.secondaryContainer
    val waxEdge = MaterialTheme.colorScheme.secondary
    Canvas(Modifier.width(160.dp).height(180.dp)) {
        val t = time
        val cx = size.width / 2f
        val bodyW = 40.dp.toPx()
        val bodyH = 95.dp.toPx()
        val top = size.height - bodyH

        if (lit) {
            val glowCenter = Offset(cx, top - 30.dp.toPx())
            drawCircle(
                Brush.radialGradient(listOf(Color(0x66FFB74D), Color.Transparent), center = glowCenter, radius = 80.dp.toPx()),
                radius = 80.dp.toPx(),
                center = glowCenter,
            )
        }
        drawRoundRect(
            Brush.horizontalGradient(listOf(wax, Color.White.copy(alpha = 0.9f), wax), startX = cx - bodyW / 2, endX = cx + bodyW / 2),
            topLeft = Offset(cx - bodyW / 2, top),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(8.dp.toPx()),
        )
        drawRoundRect(
            waxEdge.copy(alpha = 0.5f),
            topLeft = Offset(cx - bodyW / 2, top),
            size = Size(bodyW, bodyH),
            cornerRadius = CornerRadius(8.dp.toPx()),
            style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()),
        )
        drawLine(Color(0xFF3E2723), Offset(cx, top), Offset(cx, top - 8.dp.toPx()), 2.dp.toPx())

        if (lit) {
            val flicker = 1f + 0.08f * sin(t * 13f) + 0.05f * sin(t * 23.7f)
            val sway = 2.dp.toPx() * sin(t * 3.1f)
            val base = top - 4.dp.toPx()
            val tipY = base - 44.dp.toPx() * flicker
            val w = 13.dp.toPx()
            val flame = Path().apply {
                moveTo(cx + sway, tipY)
                cubicTo(cx + w, base - 22.dp.toPx(), cx + w * 0.8f, base, cx, base)
                cubicTo(cx - w * 0.8f, base, cx - w, base - 22.dp.toPx(), cx + sway, tipY)
                close()
            }
            drawPath(
                flame,
                Brush.verticalGradient(listOf(Color(0xFFFFF8D0), Color(0xFFFFC107), Color(0xFFFF6F00)), startY = tipY, endY = base),
            )
            drawOval(
                Color(0xAA4FC3F7),
                topLeft = Offset(cx - 4.dp.toPx(), base - 10.dp.toPx()),
                size = Size(8.dp.toPx(), 9.dp.toPx()),
            )
        }
    }
}
