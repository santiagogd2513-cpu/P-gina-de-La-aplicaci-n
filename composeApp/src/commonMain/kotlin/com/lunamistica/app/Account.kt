package com.lunamistica.app

import kotlin.random.Random

data class UserAccount(val email: String, val phone: String)

/** Validación de los campos del registro. Devuelve el mensaje de error o null si es válido. */
object RegistrationRules {
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun emailError(email: String): String? = when {
        email.isBlank() -> "Escribe tu correo electrónico."
        !emailRegex.matches(email.trim()) -> "El correo no es válido."
        else -> null
    }

    fun phoneError(phone: String): String? {
        val digits = phone.filter { it.isDigit() }
        val allowed = phone.all { it.isDigit() || it in "+ -()" }
        return when {
            phone.isBlank() -> "Escribe tu número de teléfono."
            !allowed || phone.count { it == '+' } > 1 || (phone.contains('+') && !phone.trim().startsWith("+")) ->
                "Usa solo números (puedes empezar con +)."
            digits.length !in 7..15 -> "El teléfono debe tener entre 7 y 15 dígitos."
            else -> null
        }
    }

    fun passwordError(password: String): String? = when {
        password.length < 8 -> "La contraseña debe tener al menos 8 caracteres."
        password.none { it.isLetter() } || password.none { it.isDigit() } -> "Usa al menos una letra y un número."
        else -> null
    }

    fun confirmError(password: String, confirm: String): String? =
        if (password != confirm) "Las contraseñas no coinciden." else null
}

/** Guarda la cuenta en el dispositivo. La contraseña nunca se guarda en texto: solo su hash con sal. */
object AccountRepository {
    private const val KEY_EMAIL = "account_email"
    private const val KEY_PHONE = "account_phone"
    private const val KEY_SALT = "account_salt"
    private const val KEY_HASH = "account_password_hash"
    private const val ITERATIONS = 10_000

    fun isRegistered(): Boolean = KeyValueStore.getString(KEY_HASH) != null

    fun current(): UserAccount? {
        val email = KeyValueStore.getString(KEY_EMAIL) ?: return null
        val phone = KeyValueStore.getString(KEY_PHONE) ?: return null
        return UserAccount(email, phone)
    }

    fun register(email: String, phone: String, password: String) {
        val salt = Sha256.hex(Random.nextBytes(16))
        KeyValueStore.putString(KEY_EMAIL, email.trim().lowercase())
        KeyValueStore.putString(KEY_PHONE, phone.trim())
        KeyValueStore.putString(KEY_SALT, salt)
        KeyValueStore.putString(KEY_HASH, hashPassword(password, salt))
    }

    fun checkPassword(password: String): Boolean {
        val salt = KeyValueStore.getString(KEY_SALT) ?: return false
        return KeyValueStore.getString(KEY_HASH) == hashPassword(password, salt)
    }

    fun hashPassword(password: String, salt: String): String {
        val saltBytes = salt.encodeToByteArray()
        var h = Sha256.digest(saltBytes + password.encodeToByteArray())
        repeat(ITERATIONS - 1) { h = Sha256.digest(h + saltBytes) }
        return Sha256.hex(h)
    }
}
