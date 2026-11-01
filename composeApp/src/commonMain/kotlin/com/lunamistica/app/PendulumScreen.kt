@file:OptIn(ExperimentalMaterial3Api::class)

package com.lunamistica.app

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// ---------- Movimiento del péndulo ----------

/** Un patrón de movimiento: posición normalizada (-1..1) en función del tiempo. */
fun interface Swing {
    fun at(t: Float): Offset
}

private object Swings {
    val idle = Swing { t -> Offset(0.05f * sin(t * 1.3f), 0.05f * cos(t * 1.1f)) }
    val search = Swing { t ->
        Offset(0.35f * sin(t * 2.1f) + 0.12f * sin(t * 5.3f), 0.35f * sin(t * 1.7f + 1f) + 0.12f * cos(t * 4.1f))
    }
    val yes = Swing { t -> Offset(0.04f * sin(t * 3f), 0.78f * sin(t * 3.2f)) }
    val no = Swing { t -> Offset(0.78f * sin(t * 3.2f), 0.04f * sin(t * 3f)) }
    val clockwise = Swing { t -> Offset(0.6f * cos(t * 2.6f), 0.6f * sin(t * 2.6f)) }
    val counterClockwise = Swing { t -> Offset(0.45f * cos(-t * 2.2f), 0.45f * sin(-t * 2.2f)) }
    val erratic = Swing { t ->
        Offset(0.8f * sin(t * 4.3f) * cos(t * 1.1f), 0.45f * sin(t * 3.1f + 0.7f))
    }
}

/** Controla la transición suave entre patrones de movimiento. */
private class PendulumController {
    var from by mutableStateOf(Swings.idle)
    var to by mutableStateOf(Swings.idle)
    val blend = Animatable(1f)

    suspend fun swingTo(next: Swing, durationMs: Int = 1200) {
        from = to
        to = next
        blend.snapTo(0f)
        blend.animateTo(1f, tween(durationMs))
    }

    fun position(t: Float): Offset {
        val k = blend.value
        val a = from.at(t)
        val b = to.at(t)
        return Offset(a.x + (b.x - a.x) * k, a.y + (b.y - a.y) * k)
    }
}

// ---------- Datos ----------

enum class PendulumAnswer(val label: String, val detail: String, val swing: Swing) {
    Yes("Sí", "El péndulo oscila de adelante hacia atrás: la energía responde afirmativamente.", Swings.yes),
    No("No", "El péndulo oscila de lado a lado: la energía responde negativamente.", Swings.no),
    Maybe("Quizás", "El péndulo gira en círculos: la respuesta aún no está definida. Pregunta más adelante o reformula.", Swings.clockwise),
}

data class Chakra(
    val name: String,
    val sanskrit: String,
    val color: Color,
    val location: String,
    val governs: String,
    val affirmation: String,
    val stone: String,
    val practice: String,
)

enum class ChakraState(val label: String, val swing: Swing) {
    Balanced("Equilibrado", Swings.clockwise),
    Blocked("Bloqueado", Swings.counterClockwise),
    Overactive("Hiperactivo", Swings.erratic),
}

object Chakras {
    val all = listOf(
        Chakra("Raíz", "Muladhara", Color(0xFFE53935), "Base de la columna", "Seguridad, estabilidad y conexión con la tierra",
            "Estoy a salvo. Estoy firmemente conectado con la tierra.", "Jaspe rojo, turmalina negra", "Camina descalzo sobre pasto o tierra durante unos minutos."),
        Chakra("Sacro", "Svadhisthana", Color(0xFFFB8C00), "Bajo el ombligo", "Creatividad, placer y emociones",
            "Me permito sentir y disfrutar la vida.", "Cornalina, piedra luna", "Baila libremente o dedica tiempo a una actividad creativa."),
        Chakra("Plexo solar", "Manipura", Color(0xFFFDD835), "Sobre el ombligo", "Poder personal, voluntad y autoestima",
            "Confío en mí y en mi poder personal.", "Citrino, ojo de tigre", "Toma sol de la mañana y practica respiraciones profundas."),
        Chakra("Corazón", "Anahata", Color(0xFF43A047), "Centro del pecho", "Amor, compasión y perdón",
            "Doy y recibo amor con libertad.", "Cuarzo rosa, aventurina verde", "Escribe tres cosas por las que sientes gratitud."),
        Chakra("Garganta", "Vishuddha", Color(0xFF1E88E5), "Garganta", "Comunicación y expresión de la verdad",
            "Expreso mi verdad con claridad y amor.", "Aguamarina, lapislázuli", "Canta, tararea o escribe lo que sientes."),
        Chakra("Tercer ojo", "Ajna", Color(0xFF3949AB), "Entre las cejas", "Intuición, visión y claridad mental",
            "Confío en mi intuición y veo con claridad.", "Amatista, sodalita", "Medita en silencio con los ojos cerrados durante 5 minutos."),
        Chakra("Corona", "Sahasrara", Color(0xFF8E24AA), "Parte superior de la cabeza", "Conexión espiritual y propósito",
            "Estoy conectado con la sabiduría del universo.", "Cuarzo blanco, amatista", "Dedica un momento a la oración o la meditación en silencio."),
    )
}

// ---------- Pantalla ----------

@Composable
fun PendulumScreen(onBack: () -> Unit) {
    var chakraMode by remember { mutableStateOf(false) }
    LazyColumn(
        Modifier.fillMaxSize().imePadding(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
    ) {
        item {
            BannerHeader(Categories.pendulum.effect, "💎", "Péndulo", "Consulta tu energía y alinea tus chakras", onBack)
        }
        item {
            Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = !chakraMode, onClick = { chakraMode = false }, label = { Text("Pregunta sí o no") })
                FilterChip(selected = chakraMode, onClick = { chakraMode = true }, label = { Text("Alineación de chakras") })
            }
        }
        item {
            if (chakraMode) ChakraAlignment() else YesNoPendulum()
        }
        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun YesNoPendulum() {
    val controller = remember { PendulumController() }
    val scope = rememberCoroutineScope()
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf<PendulumAnswer?>(null) }
    var consulting by remember { mutableStateOf(false) }

    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            "Sostén el teléfono con calma, respira profundo y concéntrate en una pregunta que pueda responderse con sí o no.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Tu pregunta") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        PendulumBoard(controller, crystal = MaterialTheme.colorScheme.primary, labels = true)
        Button(
            onClick = {
                consulting = true
                answer = null
                scope.launch {
                    controller.swingTo(Swings.search, 800)
                    delay(2600)
                    val result = PendulumAnswer.entries[Random.nextInt(PendulumAnswer.entries.size)]
                    answer = result
                    consulting = false
                    controller.swingTo(result.swing, 1600)
                }
            },
            enabled = !consulting,
            modifier = Modifier.fillMaxWidth(),
        ) { Text(if (consulting) "El péndulo está buscando…" else "Consultar al péndulo") }

        answer?.let { a ->
            Spacer(Modifier.height(12.dp))
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    if (question.isNotBlank()) {
                        Text("“${question.trim()}”", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(6.dp))
                    }
                    Text(
                        a.label,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(a.detail, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun ChakraAlignment() {
    val controller = remember { PendulumController() }
    val scope = rememberCoroutineScope()
    val results = remember { mutableStateListOf<ChakraState?>().apply { repeat(Chakras.all.size) { add(null) } } }
    var current by remember { mutableStateOf<Int?>(null) }
    var harmonizing by remember { mutableStateOf(false) }
    var busy by remember { mutableStateOf(false) }

    fun scan() {
        busy = true
        scope.launch {
            harmonizing = false
            for (i in results.indices) results[i] = null
            for (i in Chakras.all.indices) {
                current = i
                controller.swingTo(Swings.search, 600)
                delay(900)
                val roll = Random.nextFloat()
                val state = when {
                    roll < 0.55f -> ChakraState.Balanced
                    roll < 0.8f -> ChakraState.Blocked
                    else -> ChakraState.Overactive
                }
                controller.swingTo(state.swing, 900)
                delay(900)
                results[i] = state
            }
            current = null
            busy = false
            controller.swingTo(Swings.idle)
        }
    }

    fun harmonize() {
        busy = true
        scope.launch {
            harmonizing = true
            for (i in Chakras.all.indices) {
                if (results[i] == ChakraState.Balanced) continue
                current = i
                controller.swingTo(Swings.clockwise, 1000)
                delay(3500)
                results[i] = ChakraState.Balanced
            }
            current = null
            busy = false
            controller.swingTo(Swings.idle)
        }
    }

    val active = current?.let { Chakras.all[it] }
    val scanned = results.all { it != null }

    Column(Modifier.padding(horizontal = 16.dp)) {
        Text(
            "El péndulo recorrerá tus siete chakras. Giro a la derecha: equilibrado. Giro a la izquierda: bloqueado. " +
                "Movimiento irregular: hiperactivo.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        PendulumBoard(
            controller,
            crystal = active?.color ?: MaterialTheme.colorScheme.primary,
            labels = false,
            centerColor = active?.color,
        )
        Text(
            when {
                active != null && harmonizing -> "Armonizando ${active.name}… Repite: “${active.affirmation}”"
                active != null -> "Leyendo chakra ${active.name} (${active.sanskrit})…"
                scanned -> "Lectura completa"
                else -> "Pulsa el botón para comenzar"
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = ::scan, enabled = !busy, modifier = Modifier.weight(1f)) {
                Text(if (scanned) "Leer de nuevo" else "Iniciar lectura")
            }
            if (scanned && results.any { it != ChakraState.Balanced }) {
                OutlinedButton(onClick = ::harmonize, enabled = !busy, modifier = Modifier.weight(1f)) {
                    Text("Armonizar")
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Chakras.all.forEachIndexed { i, chakra ->
            ChakraRow(chakra, results[i], highlighted = current == i)
        }

        if (scanned && !busy) {
            val pending = Chakras.all.filterIndexed { i, _ -> results[i] != ChakraState.Balanced }
            SectionTitle("Recomendaciones")
            if (pending.isEmpty()) {
                Text(
                    "✨ Tu energía está alineada. Todos tus chakras están en equilibrio.",
                    style = MaterialTheme.typography.bodyLarge,
                )
            } else {
                pending.forEach { chakra ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Text(chakra.name, fontWeight = FontWeight.Bold, color = chakra.color, style = MaterialTheme.typography.titleMedium)
                            Text("Afirmación: “${chakra.affirmation}”", style = MaterialTheme.typography.bodyMedium)
                            Text("Piedras: ${chakra.stone}", style = MaterialTheme.typography.bodyMedium)
                            Text("Práctica: ${chakra.practice}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChakraRow(chakra: Chakra, state: ChakraState?, highlighted: Boolean) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (highlighted) chakra.color.copy(alpha = 0.15f) else Color.Transparent)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(if (highlighted) 30.dp else 24.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(chakra.color.copy(alpha = 0.6f), chakra.color)))
                .border(2.dp, chakra.color.copy(alpha = 0.4f), CircleShape),
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("${chakra.name} · ${chakra.sanskrit}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(chakra.governs, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(
            state?.label ?: "—",
            style = MaterialTheme.typography.labelLarge,
            color = when (state) {
                ChakraState.Balanced -> MaterialTheme.colorScheme.primary
                null -> MaterialTheme.colorScheme.outline
                else -> MaterialTheme.colorScheme.error
            },
        )
    }
}

@Composable
private fun PendulumBoard(
    controller: PendulumController,
    crystal: Color,
    labels: Boolean,
    centerColor: Color? = null,
) {
    val time by rememberShaderTime()
    val measurer = rememberTextMeasurer()
    val line = MaterialTheme.colorScheme.outline
    val text = MaterialTheme.colorScheme.onSurfaceVariant
    val string = MaterialTheme.colorScheme.onSurface
    Canvas(Modifier.fillMaxWidth().height(320.dp)) {
        val t = time
        val center = Offset(size.width / 2f, size.height * 0.62f)
        val radius = minOf(size.width * 0.42f, size.height * 0.34f)

        drawBoard(center, radius, line, centerColor)
        if (labels) drawLabels(measurer, center, radius, text)

        val p = controller.position(t)
        val bob = Offset(center.x + p.x * radius, center.y + p.y * radius)
        val anchor = Offset(size.width / 2f, 4f)

        // Sombra sobre el tablero
        drawOval(
            Color.Black.copy(alpha = 0.18f),
            topLeft = Offset(bob.x - 16.dp.toPx(), bob.y + 18.dp.toPx()),
            size = Size(32.dp.toPx(), 9.dp.toPx()),
        )
        drawLine(string.copy(alpha = 0.7f), anchor, Offset(bob.x, bob.y - 26.dp.toPx()), strokeWidth = 1.5.dp.toPx())
        drawCrystal(bob, crystal)
    }
}

private fun DrawScope.drawBoard(center: Offset, radius: Float, line: Color, centerColor: Color?) {
    centerColor?.let {
        drawCircle(
            Brush.radialGradient(listOf(it.copy(alpha = 0.45f), Color.Transparent), center = center, radius = radius),
            radius = radius,
            center = center,
        )
    }
    drawCircle(line.copy(alpha = 0.6f), radius, center, style = Stroke(1.5.dp.toPx()))
    drawCircle(line.copy(alpha = 0.35f), radius * 0.6f, center, style = Stroke(1.dp.toPx()))
    drawLine(line.copy(alpha = 0.5f), Offset(center.x, center.y - radius), Offset(center.x, center.y + radius), 1.dp.toPx())
    drawLine(line.copy(alpha = 0.5f), Offset(center.x - radius, center.y), Offset(center.x + radius, center.y), 1.dp.toPx())
}

private fun DrawScope.drawLabels(measurer: TextMeasurer, center: Offset, radius: Float, color: Color) {
    val style = TextStyle(color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    fun label(text: String, at: Offset) {
        val layout = measurer.measure(text, style)
        drawText(layout, topLeft = Offset(at.x - layout.size.width / 2f, at.y - layout.size.height / 2f))
    }
    label("SÍ", Offset(center.x + 16.dp.toPx(), center.y - radius - 12.dp.toPx()))
    label("SÍ", Offset(center.x + 16.dp.toPx(), center.y + radius + 12.dp.toPx()))
    label("NO", Offset(center.x - radius - 4.dp.toPx(), center.y - 14.dp.toPx()))
    label("NO", Offset(center.x + radius + 4.dp.toPx(), center.y - 14.dp.toPx()))
    label("QUIZÁS", Offset(center.x + radius * 0.6f * 0.72f + 18.dp.toPx(), center.y + radius * 0.6f * 0.72f))
}

private fun DrawScope.drawCrystal(bob: Offset, color: Color) {
    val w = 14.dp.toPx()
    val top = bob.y - 26.dp.toPx()
    val mid = bob.y - 10.dp.toPx()
    val tip = bob.y + 16.dp.toPx()
    drawCircle(
        Brush.radialGradient(listOf(color.copy(alpha = 0.45f), Color.Transparent), center = bob, radius = 40.dp.toPx()),
        radius = 40.dp.toPx(),
        center = bob,
    )
    val body = Path().apply {
        moveTo(bob.x, top)
        lineTo(bob.x + w, mid)
        lineTo(bob.x, tip)
        lineTo(bob.x - w, mid)
        close()
    }
    drawPath(body, Brush.linearGradient(listOf(Color.White, color, color.copy(alpha = 0.8f)), start = Offset(bob.x - w, top), end = Offset(bob.x + w, tip)))
    val facet = Path().apply {
        moveTo(bob.x, top)
        lineTo(bob.x + w, mid)
        lineTo(bob.x, mid + 4.dp.toPx())
        close()
    }
    drawPath(facet, Color.White.copy(alpha = 0.35f))
}
