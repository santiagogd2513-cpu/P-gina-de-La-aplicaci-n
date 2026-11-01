package com.lunamistica.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Gold = Color(0xFFF2C66D)

/** Ventanas de planes: Premium (con botón Comprar) y, debajo, el plan gratuito. */
@Composable
fun PlanChooser(onBuy: () -> Unit, onFree: (() -> Unit)?) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // ---------- Premium ----------
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(listOf(Color(0xFF3A1E72), Color(0xFF6B3FA0), Color(0xFF2A1250))))
                .border(2.dp, Gold, RoundedCornerShape(24.dp))
                .padding(20.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("👑", fontSize = 30.sp)
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text("Premium", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Gold)
                    Text("Recomendado", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.8f))
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(Plans.priceLabel, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = Color.White)
                Text(" COP / mes", style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.85f), modifier = Modifier.padding(bottom = 6.dp))
            }
            Spacer(Modifier.height(10.dp))
            Plans.premiumBenefits.forEach { Benefit(it, Color.White, Gold) }
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onBuy,
                colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color(0xFF2B1F00)),
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text("Comprar", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }

        // ---------- Gratis ----------
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                .padding(20.dp),
        ) {
            Text("Plan gratuito", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("${Plans.formatCop(0)} COP", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))
            Plans.freeBenefits.forEach { Benefit(it, MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.primary) }
            if (onFree != null) {
                Spacer(Modifier.height(12.dp))
                OutlinedButton(onClick = onFree, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    Text("Continuar con el plan gratuito")
                }
            }
        }
    }
}

@Composable
private fun Benefit(text: String, color: Color, accent: Color) {
    Row(Modifier.padding(vertical = 3.dp)) {
        Text("✓", color = accent, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 10.dp))
        Text(text, color = color, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * Pantalla de pago: el usuario elige el medio de pago y continúa a la pasarela.
 * La app NUNCA pide ni guarda datos de tarjeta: eso lo hace la pasarela en su página segura.
 * Mientras no haya pasarela configurada ([Plans.PAYMENT_URL] = null) funciona en modo de prueba.
 */
@Composable
fun PaymentContent(onPaid: () -> Unit, onBack: () -> Unit) {
    var method by remember { mutableStateOf(PaymentMethods.first().id) }
    var done by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    if (done) {
        Column(Modifier.fillMaxWidth().padding(vertical = 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("👑", fontSize = 56.sp)
            Text("¡Ya eres Premium!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Disfruta de lecturas ilimitadas durante ${Plans.PREMIUM_DAYS} días.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(20.dp))
            Button(onClick = onPaid, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Continuar", style = MaterialTheme.typography.titleMedium)
            }
        }
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (Plans.isTestMode) {
            Text(
                "🧪 MODO DE PRUEBA · Los pagos aún no están conectados. No se hará ningún cobro.",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .padding(12.dp),
            )
        }

        // Resumen
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("👑", fontSize = 26.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Luna Mística Premium", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                Text("Mensual · ${Plans.PREMIUM_DAYS} días", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
            }
            Text("${Plans.priceLabel} COP", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
        }

        Text("Elige tu medio de pago", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp))
        PaymentMethods.forEach { m ->
            val selected = method == m.id
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer)
                    .plainClickable { method = m.id }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(selected = selected, onClick = { method = m.id })
                Box(
                    Modifier.size(38.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceContainerHighest),
                    contentAlignment = Alignment.Center,
                ) { Text(m.emoji, fontSize = 20.sp) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(m.name, fontWeight = FontWeight.SemiBold)
                    Text(m.detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Text(
            "🔒 Pago seguro: los datos de tu tarjeta o cuenta se ingresan en la página de la pasarela de pagos. " +
                "Luna Mística nunca los ve ni los guarda.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 6.dp),
        )

        Button(
            onClick = {
                val url = Plans.PAYMENT_URL
                if (url == null) {
                    // Modo de prueba: se activa Premium sin cobro.
                    done = true
                } else {
                    runCatching { uriHandler.openUri(url) }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            Text(
                if (Plans.isTestMode) "Simular pago de ${Plans.priceLabel} (prueba)" else "Pagar ${Plans.priceLabel} COP",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(48.dp)) {
            Text("Volver a los planes")
        }
    }
}

/** Pantalla de planes accesible desde la app (botón "Hazte Premium"). */
@Composable
fun PremiumScreen(onBack: () -> Unit) {
    var paying by remember { mutableStateOf(false) }
    var refresh by remember { mutableStateOf(0) }
    PlatformBackHandler(enabled = paying) { paying = false }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilledTonalIconButton(onClick = { if (paying) paying = false else onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(Modifier.width(12.dp))
            Text(if (paying) "Pago" else "Planes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        val until = remember(refresh) { PlanRepository.premiumUntil() }
        if (remember(refresh) { PlanRepository.isPremium() } && !paying) {
            Text(
                "👑 Tu plan Premium está activo hasta el $until.",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        if (paying) {
            PaymentContent(
                onPaid = {
                    PlanRepository.activatePremium()
                    paying = false
                    refresh++
                    onBack()
                },
                onBack = { paying = false },
            )
        } else {
            PlanChooser(onBuy = { paying = true }, onFree = null)
        }
    }
}
