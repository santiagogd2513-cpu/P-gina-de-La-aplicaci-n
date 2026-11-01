package com.lunamistica.app

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class NumberResult(val label: String, val number: Int, val title: String, val text: String)

@Composable
fun NumerologyScreen(onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var results by remember { mutableStateOf<List<NumberResult>>(emptyList()) }
    val focus = LocalFocusManager.current

    fun calculate() {
        val d = day.toIntOrNull()
        val m = month.toIntOrNull()
        val y = year.toIntOrNull()
        if (name.none { Numerology.normalize(it) != null }) {
            error = "Escribe tu nombre completo."
            return
        }
        if (d == null || m == null || y == null || !Numerology.isValidDate(d, m, y)) {
            error = "Revisa tu fecha de nacimiento (día, mes y año)."
            return
        }
        error = null
        focus.clearFocus()
        val currentYear = today().year
        fun meaning(label: String, n: Int): NumberResult {
            val mng = Numerology.meanings.getValue(n)
            return NumberResult(label, n, mng.title, mng.text)
        }
        val personalYear = Numerology.personalYear(d, m, currentYear)
        results = buildList {
            add(meaning("Camino de vida", Numerology.lifePath(d, m, y)))
            add(meaning("Expresión (destino)", Numerology.expression(name)))
            Numerology.soul(name).takeIf { it > 0 }?.let { add(meaning("Número del alma", it)) }
            Numerology.personality(name).takeIf { it > 0 }?.let { add(meaning("Personalidad", it)) }
            add(meaning("Día de nacimiento", Numerology.birthday(d)))
            add(
                NumberResult(
                    "Año personal $currentYear",
                    personalYear,
                    "Tu energía este año",
                    Numerology.personalYearMeanings.getValue(personalYear),
                )
            )
        }
    }

    LazyColumn(
        Modifier.fillMaxSize().imePadding(),
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
    ) {
        item {
            BannerHeader(Categories.numerology.effect, "🔢", "Numerología", "Los números que guían tu vida", onBack)
        }
        item {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre completo de nacimiento") },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                Text("Fecha de nacimiento", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DateField(day, { day = it.take(2) }, "Día", Modifier.weight(1f))
                    DateField(month, { month = it.take(2) }, "Mes", Modifier.weight(1f))
                    DateField(year, { year = it.take(4) }, "Año", Modifier.weight(1.4f))
                }
                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 6.dp))
                }
                Spacer(Modifier.height(12.dp))
                Button(onClick = ::calculate, modifier = Modifier.fillMaxWidth()) { Text("Calcular mis números") }
            }
        }
        items(results) { r ->
            NumberCard(r, Modifier.padding(horizontal = 16.dp, vertical = 6.dp))
        }
        item {
            Text(
                "Se usa el sistema pitagórico: A, J, S = 1 · B, K, T = 2 · C, L, U = 3 · D, M, V = 4 · E, N, Ñ, W = 5 · " +
                    "F, O, X = 6 · G, P, Y = 7 · H, Q, Z = 8 · I, R = 9. Los números 11, 22 y 33 son maestros.",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DateField(value: String, onChange: (String) -> Unit, label: String, modifier: Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = { onChange(it.filter(Char::isDigit)) },
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
    )
}

@Composable
private fun NumberCard(r: NumberResult, modifier: Modifier) {
    val colors = MaterialTheme.colorScheme
    Card(
        modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceContainer),
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(
                Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(colors.primary, colors.tertiary))),
                contentAlignment = Alignment.Center,
            ) {
                Text("${r.number}", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = colors.onPrimary)
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(r.label.uppercase(), style = MaterialTheme.typography.labelMedium, color = colors.secondary)
                Text(r.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(r.text, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
