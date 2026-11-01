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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.delay

private const val MAX_ATTEMPTS = 5
private const val LOCK_SECONDS = 30

/** Inicio de sesión con la cuenta guardada en este dispositivo. */
@Composable
fun LoginScreen(onLoggedIn: () -> Unit, onRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var failures by remember { mutableIntStateOf(0) }
    var lockSeconds by remember { mutableIntStateOf(0) }
    var showForgot by remember { mutableStateOf(false) }
    val focus = LocalFocusManager.current
    val time by rememberShaderTime(speed = 0.6f)
    val savedName = remember { AccountRepository.current()?.firstName.orEmpty() }

    // Tras varios intentos fallidos, espera unos segundos antes de permitir otro.
    LaunchedEffect(lockSeconds > 0) {
        while (lockSeconds > 0) {
            delay(1000)
            lockSeconds--
        }
    }

    fun submit() {
        if (lockSeconds > 0) return
        if (email.isBlank() || password.isEmpty()) {
            error = "Escribe tu correo y tu contraseña."
            return
        }
        when (AccountRepository.login(email, password)) {
            LoginResult.Success -> {
                focus.clearFocus()
                onLoggedIn()
            }
            LoginResult.NoAccount -> error = "No hay ninguna cuenta con ese correo en este dispositivo."
            LoginResult.WrongPassword -> {
                failures++
                password = ""
                if (failures >= MAX_ATTEMPTS) {
                    failures = 0
                    lockSeconds = LOCK_SECONDS
                    error = "Demasiados intentos. Espera $LOCK_SECONDS segundos."
                } else {
                    error = "Contraseña incorrecta. Te quedan ${MAX_ATTEMPTS - failures} intentos."
                }
            }
        }
    }

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
                Text("🌙", fontSize = 40.sp)
                Text(
                    if (savedName.isNotBlank()) "Hola de nuevo, $savedName" else "Iniciar sesión",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                Text(
                    "Entra con tu correo y tu contraseña",
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
            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim(); error = null },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; error = null },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                trailingIcon = {
                    TextButton(onClick = { showPassword = !showPassword }) {
                        Text(if (showPassword) "Ocultar" else "Ver")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            )
            error?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                )
            }
            TextButton(onClick = { showForgot = true }, modifier = Modifier.align(Alignment.End)) {
                Text("¿Olvidaste tu contraseña?")
            }

            Button(
                onClick = ::submit,
                enabled = lockSeconds == 0,
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text(
                    if (lockSeconds > 0) "Espera $lockSeconds s" else "Iniciar sesión",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onRegister, modifier = Modifier.fillMaxWidth().height(52.dp)) {
                Text("¿No tienes cuenta? Regístrate")
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "La cuenta se guarda en este dispositivo. Para entrar en otro celular o computador, crea una cuenta allí.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }

    if (showForgot) {
        AlertDialog(
            onDismissRequest = { showForgot = false },
            title = { Text("¿Olvidaste tu contraseña?") },
            text = {
                Text(
                    "Por seguridad, tu contraseña se guarda cifrada y no se puede recuperar. " +
                        "Puedes crear una cuenta nueva en este dispositivo; la anterior se reemplazará."
                )
            },
            confirmButton = {
                TextButton(onClick = { showForgot = false; onRegister() }) { Text("Crear cuenta nueva") }
            },
            dismissButton = {
                TextButton(onClick = { showForgot = false }) { Text("Cancelar") }
            },
        )
    }
}
