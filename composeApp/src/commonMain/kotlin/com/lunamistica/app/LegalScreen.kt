package com.lunamistica.app

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** Muestra un documento legal completo (título, secciones y párrafos). */
@Composable
fun LegalDocumentView(document: LegalDocument) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(Modifier.padding(18.dp)) {
            Text(
                document.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                "Versión ${Legal.VERSION} · Actualizada el ${Legal.UPDATED}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            document.sections.forEach { section ->
                Spacer(Modifier.height(14.dp))
                Text(section.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                section.paragraphs.forEach { p ->
                    Text(
                        p,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}

/**
 * Los dos documentos, la casilla de aceptación y los botones. Se usa como tercer paso
 * del registro y como pantalla propia cuando cambia la versión de los textos.
 * El botón "Comenzar" solo se activa al marcar la casilla.
 */
@Composable
fun TermsAcceptance(onStart: () -> Unit, onBack: (() -> Unit)?) {
    var accepted by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text(
            "Lee los Términos y Condiciones y la Política de Privacidad. Para usar Luna Mística debes aceptarlos.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        LegalDocumentView(Legal.terms)
        LegalDocumentView(Legal.privacy)

        HorizontalDivider()

        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (accepted) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceContainerHigh
                )
                .plainClickable { accepted = !accepted }
                .padding(horizontal = 6.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = accepted, onCheckedChange = { accepted = it })
            Text(
                "He leído y acepto todos los Términos y Condiciones y la Política de Privacidad.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (onBack != null) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f).height(52.dp)) {
                    Text("Atrás")
                }
            }
            Button(
                onClick = onStart,
                enabled = accepted,
                modifier = Modifier.weight(1.6f).height(52.dp),
            ) {
                Text("Comenzar ✨", style = MaterialTheme.typography.titleMedium)
            }
        }
        if (!accepted) {
            Text(
                "Marca la casilla para activar el botón Comenzar.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

/** Pantalla propia para quien ya tenía cuenta pero aún no aceptó la versión actual de los textos. */
@Composable
fun TermsUpdateScreen(onAccepted: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(20.dp),
    ) {
        Text(
            "Términos y privacidad",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(12.dp))
        TermsAcceptance(
            onStart = {
                AccountRepository.acceptTerms()
                onAccepted()
            },
            onBack = null,
        )
    }
}

/** Lectura de los textos desde la pantalla de inicio (sin casilla). */
@Composable
fun LegalReadScreen(onBack: () -> Unit) {
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
            FilledTonalIconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(Modifier.padding(start = 12.dp))
            Text("Términos y privacidad", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        LegalDocumentView(Legal.terms)
        LegalDocumentView(Legal.privacy)
        AccountRepository.termsAcceptedOn()?.let {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    "Aceptaste la versión ${Legal.VERSION} el $it.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
