@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.lunamistica.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.datetime.LocalDate

/** Registro obligatorio en dos pasos que se muestra la primera vez que se abre la app. */
@Composable
fun RegistrationScreen(onRegistered: () -> Unit, onLogin: () -> Unit) {
    // Paso 1: cuenta
    var email by remember { mutableStateOf("") }
    var country by remember { mutableStateOf(Countries.default) }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var submitted1 by remember { mutableStateOf(false) }
    var pickingCountry by remember { mutableStateOf(false) }

    // Paso 2: datos personales
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf<String?>(null) }
    var submitted2 by remember { mutableStateOf(false) }

    var step by remember { mutableStateOf(1) }
    val existingAccount = remember { AccountRepository.isRegistered() }
    val focus = LocalFocusManager.current
    val time by rememberShaderTime(speed = 0.6f)
    val today = remember { today() }

    val emailError = RegistrationRules.emailError(email)
    val phoneError = RegistrationRules.phoneError(phone)
    val passwordError = RegistrationRules.passwordError(password)
    val confirmError = RegistrationRules.confirmError(password, confirm)
    val step1Valid = listOf(emailError, phoneError, passwordError, confirmError).all { it == null }

    val firstNameError = RegistrationRules.nameError(firstName, "nombre")
    val lastNameError = RegistrationRules.nameError(lastName, "apellido")
    val birthError = RegistrationRules.birthDateError(day, month, year, today)
    val genderError = RegistrationRules.genderError(gender)
    val step2Valid = listOf(firstNameError, lastNameError, birthError, genderError).all { it == null }

    fun next() {
        submitted1 = true
        if (!step1Valid) return
        focus.clearFocus()
        step = 2
    }

    fun toTerms() {
        submitted2 = true
        if (!step2Valid) return
        focus.clearFocus()
        step = 3
    }

    fun finish() {
        if (!step1Valid || !step2Valid) return
        AccountRepository.register(
            Credentials(email, country, phone, password),
            Profile(firstName, lastName, LocalDate(year.toInt(), month.toInt(), day.toInt()), gender!!),
        )
        AccountRepository.acceptTerms()
        onRegistered()
    }

    // En los pasos 2 y 3, el botón atrás del sistema vuelve al paso anterior.
    PlatformBackHandler(enabled = step > 1) { step-- }

    val passwordTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(Modifier.fillMaxWidth().height(220.dp)) {
            ShaderSurface(ShaderLibrary.cosmos, { time }, { null }, Modifier.matchParentSize())
            Box(
                Modifier.matchParentSize().background(
                    Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)))
                )
            )
            Column(
                Modifier
                    .align(Alignment.BottomCenter)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("🌙", fontSize = 40.sp)
                Text(
                    when (step) {
                        1 -> "Bienvenida a Luna Mística"
                        2 -> "Cuéntanos sobre ti"
                        else -> "Términos y privacidad"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                Text(
                    when (step) {
                        1 -> "Crea tu cuenta para comenzar"
                        2 -> "Así personalizamos tus lecturas"
                        else -> "Lee y acepta para comenzar"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                )
            }
        }

        Column(
            Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .windowInsetsPadding(WindowInsets.navigationBars),
        ) {
            Text(
                "Paso $step de 3",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(progress = { step / 3f }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))

            if (step == 1) {
                RegistrationField(
                    value = email,
                    onChange = { email = it.trim() },
                    label = "Correo electrónico",
                    error = emailError.takeIf { submitted1 },
                    keyboardType = KeyboardType.Email,
                )
                Row(verticalAlignment = Alignment.Top) {
                    OutlinedButton(
                        onClick = { pickingCountry = true },
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(top = 12.dp).height(56.dp),
                    ) {
                        Text("${country.label} ▾", style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.weight(1f)) {
                        RegistrationField(
                            value = phone,
                            onChange = { phone = it.filter { c -> c.isDigit() || c == ' ' } },
                            label = "Número de teléfono",
                            placeholder = "300 123 4567",
                            error = phoneError.takeIf { submitted1 },
                            keyboardType = KeyboardType.Phone,
                        )
                    }
                }
                Text(
                    "País: ${country.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                )
                RegistrationField(
                    value = password,
                    onChange = { password = it },
                    label = "Contraseña",
                    error = passwordError.takeIf { submitted1 },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = passwordTransformation,
                    supporting = "Mínimo 8 caracteres, con al menos una letra y un número.",
                    trailing = {
                        TextButton(onClick = { showPassword = !showPassword }) {
                            Text(if (showPassword) "Ocultar" else "Ver")
                        }
                    },
                )
                RegistrationField(
                    value = confirm,
                    onChange = { confirm = it },
                    label = "Confirmar contraseña",
                    error = confirmError.takeIf { submitted1 },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = passwordTransformation,
                    imeAction = ImeAction.Done,
                    onDone = ::next,
                )
                Spacer(Modifier.height(12.dp))
                Button(onClick = ::next, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                    Text("Siguiente", style = MaterialTheme.typography.titleMedium)
                }
                if (submitted1 && !step1Valid) ErrorHint()
                if (existingAccount) {
                    Text(
                        "Ya hay una cuenta en este dispositivo. Si creas una nueva, la anterior se reemplazará.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    )
                }
                TextButton(onClick = onLogin, modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                    Text("¿Ya tienes una cuenta? Inicia sesión")
                }
            } else if (step == 2) {
                RegistrationField(
                    value = firstName,
                    onChange = { firstName = it },
                    label = "Nombre",
                    error = firstNameError.takeIf { submitted2 },
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                )
                RegistrationField(
                    value = lastName,
                    onChange = { lastName = it },
                    label = "Apellido",
                    error = lastNameError.takeIf { submitted2 },
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Words,
                )

                Text(
                    "Fecha de nacimiento",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DigitsField(day, { day = it.take(2) }, "Día", "DD", Modifier.weight(1f))
                    DigitsField(month, { month = it.take(2) }, "Mes", "MM", Modifier.weight(1f))
                    DigitsField(year, { year = it.take(4) }, "Año", "AAAA", Modifier.weight(1.4f))
                }
                if (submitted2 && birthError != null) FieldError(birthError)

                Text(
                    "Género",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp),
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Genders.forEach { option ->
                        FilterChip(
                            selected = gender == option,
                            onClick = { gender = option },
                            label = { Text(option) },
                        )
                    }
                }
                if (submitted2 && genderError != null) FieldError(genderError)

                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = { step = 1 }, modifier = Modifier.weight(1f).height(52.dp)) {
                        Text("Atrás")
                    }
                    Button(onClick = ::toTerms, modifier = Modifier.weight(1.6f).height(52.dp)) {
                        Text("Siguiente", style = MaterialTheme.typography.titleMedium)
                    }
                }
                if (submitted2 && !step2Valid) ErrorHint()
            } else {
                TermsAcceptance(onStart = ::finish, onBack = { step = 2 })
            }

            Spacer(Modifier.height(12.dp))
            Text(
                "Tus datos se guardan de forma privada en este dispositivo. La contraseña se guarda cifrada.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    if (pickingCountry) {
        CountryPickerDialog(
            onSelect = { country = it; pickingCountry = false },
            onDismiss = { pickingCountry = false },
        )
    }
}

@Composable
private fun ErrorHint() {
    Text(
        "Revisa los campos marcados en rojo.",
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun FieldError(text: String) {
    Text(
        text,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp),
    )
}

@Composable
private fun DigitsField(value: String, onChange: (String) -> Unit, label: String, placeholder: String, modifier: Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = { onChange(it.filter(Char::isDigit)) },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        modifier = modifier,
    )
}

/** Lista de todos los países con buscador; al elegir uno se usa su código telefónico. */
@Composable
private fun CountryPickerDialog(onSelect: (Country) -> Unit, onDismiss: () -> Unit) {
    var query by remember { mutableStateOf("") }
    val results = remember(query) { Countries.search(query) }
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth(0.92f).fillMaxHeight(0.85f),
        ) {
            Column(Modifier.padding(top = 20.dp)) {
                Text(
                    "Elige tu país",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Buscar país o código") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                )
                LazyColumn(Modifier.weight(1f)) {
                    if (query.isBlank()) {
                        item { GroupLabel("Más usados") }
                        items(Countries.popular, key = { "p-" + it.iso }) { CountryRow(it, onSelect) }
                        item { HorizontalDivider(Modifier.padding(vertical = 4.dp)) }
                        item { GroupLabel("Todos los países") }
                    }
                    items(results, key = { it.iso }) { CountryRow(it, onSelect) }
                    if (results.isEmpty()) {
                        item {
                            Text(
                                "No se encontró ningún país.",
                                modifier = Modifier.padding(20.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
                TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End).padding(8.dp)) {
                    Text("Cancelar")
                }
            }
        }
    }
}

@Composable
private fun GroupLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
    )
}

@Composable
private fun CountryRow(country: Country, onSelect: (Country) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onSelect(country) }
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(country.flag, fontSize = 24.sp)
        Spacer(Modifier.width(14.dp))
        Text(country.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(
            "+${country.dialCode}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
private fun RegistrationField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    error: String?,
    keyboardType: KeyboardType,
    placeholder: String? = null,
    supporting: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Next,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    onDone: () -> Unit = {},
    trailing: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        isError = error != null,
        supportingText = (error ?: supporting)?.let { { Text(it) } },
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = capitalization,
        ),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        trailingIcon = trailing,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    )
}
