package com.lunamistica.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Registro obligatorio que se muestra la primera vez que se abre la app. */
@Composable
fun RegistrationScreen(onRegistered: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    val focus = LocalFocusManager.current
    val time by rememberShaderTime(speed = 0.6f)

    val emailError = RegistrationRules.emailError(email)
    val phoneError = RegistrationRules.phoneError(phone)
    val passwordError = RegistrationRules.passwordError(password)
    val confirmError = RegistrationRules.confirmError(password, confirm)
    val valid = listOf(emailError, phoneError, passwordError, confirmError).all { it == null }

    fun submit() {
        submitted = true
        if (!valid) return
        focus.clearFocus()
        AccountRepository.register(email, phone, password)
        onRegistered()
    }

    val passwordTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState()),
    ) {
        Box(Modifier.fillMaxWidth().height(240.dp)) {
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
                Text("🌙", fontSize = 44.sp)
                Text(
                    "Bienvenida a Luna Mística",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                Text(
                    "Crea tu cuenta para comenzar",
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
            RegistrationField(
                value = email,
                onChange = { email = it.trim() },
                label = "Correo electrónico",
                error = emailError.takeIf { submitted },
                keyboardType = KeyboardType.Email,
            )
            RegistrationField(
                value = phone,
                onChange = { phone = it },
                label = "Número de teléfono",
                placeholder = "+57 300 123 4567",
                error = phoneError.takeIf { submitted },
                keyboardType = KeyboardType.Phone,
            )
            RegistrationField(
                value = password,
                onChange = { password = it },
                label = "Contraseña",
                error = passwordError.takeIf { submitted },
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
                error = confirmError.takeIf { submitted },
                keyboardType = KeyboardType.Password,
                visualTransformation = passwordTransformation,
                imeAction = ImeAction.Done,
                onDone = ::submit,
            )

            Spacer(Modifier.height(12.dp))
            Button(onClick = ::submit, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("Crear cuenta", style = MaterialTheme.typography.titleMedium)
            }
            if (submitted && !valid) {
                Text(
                    "Revisa los campos marcados en rojo.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                )
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { onDone() }),
        trailingIcon = trailing,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
    )
}
