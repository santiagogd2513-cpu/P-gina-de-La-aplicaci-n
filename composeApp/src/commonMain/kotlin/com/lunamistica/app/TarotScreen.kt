@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.lunamistica.app

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

private val CardShape = RoundedCornerShape(14.dp)
private val Gold = Color(0xFFF2C66D)

private fun drawSpread(spread: Spread): List<DrawnCard> {
    val random = if (spread == Spread.Daily) Random(today().toEpochDays()) else Random.Default
    val cards = TarotDeck.majorArcana.shuffled(random)
    return spread.positions.mapIndexed { i, position ->
        DrawnCard(cards[i], reversed = random.nextFloat() < 0.3f, position = position)
    }
}

@Composable
fun TarotScreen(onBack: () -> Unit) {
    var spread by remember { mutableStateOf(Spread.Daily) }
    var question by remember { mutableStateOf("") }
    var drawn by remember { mutableStateOf<List<DrawnCard>>(emptyList()) }
    val revealed = remember { mutableStateListOf<Int>() }

    fun deal() {
        revealed.clear()
        drawn = drawSpread(spread)
    }

    LazyColumn(
        Modifier.fillMaxSize().imePadding(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
    ) {
        item {
            BannerHeader(Categories.tarot.effect, "🔮", "Tarot", "Los 22 Arcanos Mayores te guían", onBack)
        }
        item {
            Column(Modifier.padding(horizontal = 16.dp)) {
                SectionTitle("Elige tu tirada")
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Spread.entries.forEach { s ->
                        FilterChip(
                            selected = spread == s,
                            onClick = { spread = s; drawn = emptyList(); revealed.clear() },
                            label = { Text(s.label) },
                        )
                    }
                }
                if (spread != Spread.Daily) {
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("Tu pregunta (opcional)") },
                        placeholder = { Text("Concéntrate en lo que quieres saber…") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Spacer(Modifier.height(12.dp))
                Button(onClick = ::deal, modifier = Modifier.fillMaxWidth()) {
                    Text(if (spread == Spread.Daily) "Ver mi carta del día" else "Barajar y tirar las cartas")
                }
            }
        }

        if (drawn.isNotEmpty()) {
            item {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                    ) {
                        drawn.forEachIndexed { i, d ->
                            Column(
                                Modifier.weight(1f).widthIn(max = 150.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    d.position,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                )
                                Spacer(Modifier.height(6.dp))
                                FlippableCard(d, revealed = i in revealed, onClick = { if (i !in revealed) revealed.add(i) })
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    if (revealed.size < drawn.size) {
                        Text(
                            "Toca cada carta para revelarla",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        TextButton(onClick = { revealed.clear(); revealed.addAll(drawn.indices) }) {
                            Text("Revelar todas")
                        }
                    }
                }
            }
            items(drawn.filterIndexed { i, _ -> i in revealed }) { d ->
                Interpretation(d, Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
            }
        }

        item {
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FlippableCard(drawn: DrawnCard, revealed: Boolean, onClick: () -> Unit) {
    val rotation by animateFloatAsState(if (revealed) 180f else 0f, tween(700), label = "flip")
    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(0.6f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 14f * density
            }
            .clickable(onClick = onClick),
    ) {
        if (rotation <= 90f) {
            CardBack(Modifier.fillMaxSize())
        } else {
            CardFace(drawn, Modifier.fillMaxSize().graphicsLayer { rotationY = 180f })
        }
    }
}

@Composable
private fun CardBack(modifier: Modifier) {
    val time by rememberShaderTime(speed = 0.6f)
    Box(modifier.clip(CardShape).border(2.dp, Gold, CardShape), contentAlignment = Alignment.Center) {
        ShaderSurface(ShaderLibrary.cosmos, { time }, { null }, Modifier.matchParentSize())
        Box(Modifier.matchParentSize().padding(6.dp).border(1.dp, Gold.copy(alpha = 0.6f), RoundedCornerShape(10.dp)))
        Text("☾ ✦ ☽", color = Gold, fontSize = 18.sp)
    }
}

@Composable
private fun CardFace(drawn: DrawnCard, modifier: Modifier) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier
            .clip(CardShape)
            .background(Brush.verticalGradient(listOf(colors.secondaryContainer, colors.surfaceContainerLowest)))
            .border(2.dp, colors.secondary, CardShape)
            .padding(8.dp),
    ) {
        Column(
            Modifier.fillMaxSize().rotate(if (drawn.reversed) 180f else 0f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(drawn.card.roman, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = colors.secondary)
            Spacer(Modifier.weight(1f))
            Text(drawn.card.emoji, fontSize = 40.sp)
            Spacer(Modifier.weight(1f))
            Text(
                drawn.card.name,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                color = colors.onSurface,
            )
        }
    }
}

@Composable
private fun Interpretation(d: DrawnCard, modifier: Modifier) {
    Card(
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(d.position.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
            Text(
                "${d.card.emoji}  ${d.card.name}" + if (d.reversed) " (invertida)" else "",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(d.card.keywords, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(if (d.reversed) d.card.reversed else d.card.upright, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
