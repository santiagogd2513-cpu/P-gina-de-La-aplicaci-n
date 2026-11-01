@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.lunamistica.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(navigate: (Screen) -> Unit, onLogout: () -> Unit) {
    val time by rememberShaderTime()
    val moon = remember { Moon.phase() }
    val uriHandler = LocalUriHandler.current
    val firstName = remember { AccountRepository.current()?.firstName.orEmpty() }
    // Saludo, frase, consejo lunar y orden de las categorías cambian cada vez que se abre el inicio.
    val greeting = remember { Variety.greetings.random() }
    val tagline = remember { Variety.taglines.random() }
    val moonAdvice = remember { moon.randomAdvice() }
    val categories = remember { Categories.all.shuffled() }
    var confirmLogout by remember { mutableStateOf(false) }

    if (confirmLogout) {
        AlertDialog(
            onDismissRequest = { confirmLogout = false },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Quieres cerrar sesión? Para volver a entrar necesitarás tu correo y tu contraseña.") },
            confirmButton = {
                TextButton(onClick = { confirmLogout = false; onLogout() }) { Text("Cerrar sesión") }
            },
            dismissButton = {
                TextButton(onClick = { confirmLogout = false }) { Text("Cancelar") }
            },
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(Modifier.padding(top = 8.dp, bottom = 4.dp)) {
                Text(
                    "Luna Mística",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (firstName.isNotBlank()) "$greeting, $firstName 🌙" else "$greeting 🌙",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = { confirmLogout = true }) { Text("Cerrar sesión") }
                }
                Text(
                    tagline,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            val premium = remember { PlanRepository.isPremium() }
            Card(
                onClick = { navigate(Screen.Premium) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (premium) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerHigh
                ),
            ) {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(if (premium) "👑" else "✨", fontSize = 24.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            if (premium) "Plan Premium activo" else "Plan gratuito",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            if (premium) "Lecturas ilimitadas hasta el ${PlanRepository.premiumUntil()}"
                            else "${Plans.FREE_READINGS} lecturas de tarot cada ${Plans.FREE_WINDOW_HOURS} horas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (!premium) {
                        Text("Mejorar →", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            ) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(moon.emoji, fontSize = 44.sp)
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            "Hoy: ${moon.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        Text(
                            moonAdvice,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }
        }

        items(categories, key = { it.title }) { category ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.9f)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable {
                        navigate(category.screen)
                        // Abre WhatsApp con el aviso ya escrito para el número de contacto.
                        runCatching { uriHandler.openUri(Contact.whatsappUrl(category.title, AccountRepository.current())) }
                    },
            ) {
                ShaderSurface(category.effect, { time }, { null }, Modifier.matchParentSize())
                Box(
                    Modifier.matchParentSize().background(
                        Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)))
                    )
                )
                Column(Modifier.align(Alignment.BottomStart).padding(14.dp)) {
                    Text(category.emoji, fontSize = 30.sp)
                    Text(
                        category.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                    Text(
                        category.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            TextButton(onClick = { navigate(Screen.LegalInfo) }, modifier = Modifier.fillMaxWidth()) {
                Text("Términos y Condiciones · Política de Privacidad", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
