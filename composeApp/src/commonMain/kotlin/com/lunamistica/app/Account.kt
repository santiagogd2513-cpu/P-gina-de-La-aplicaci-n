package com.lunamistica.app

import kotlinx.datetime.LocalDate
import kotlin.random.Random

data class UserAccount(
    val email: String,
    /** Teléfono completo con código de país, p. ej. "+57 3160100727". */
    val phone: String,
    val firstName: String = "",
    val lastName: String = "",
    val birthDate: LocalDate? = null,
    val gender: String = "",
) {
    val fullName: String get() = "$firstName $lastName".trim()
}

/** Datos del paso 1 del registro. */
data class Credentials(val email: String, val country: Country, val phoneNumber: String, val password: String) {
    val fullPhone: String get() = "+${country.dialCode} ${phoneNumber.filter { it.isDigit() }}"
}

/** Datos del paso 2 del registro. */
data class Profile(val firstName: String, val lastName: String, val birthDate: LocalDate, val gender: String)

val Genders = listOf("Femenino", "Masculino", "Otro", "Prefiero no decirlo")

/** Validación de los campos del registro. Devuelve el mensaje de error o null si es válido. */
object RegistrationRules {
    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun emailError(email: String): String? = when {
        email.isBlank() -> "Escribe tu correo electrónico."
        !emailRegex.matches(email.trim()) -> "El correo no es válido."
        else -> null
    }

    /** Número de teléfono sin el código de país. */
    fun phoneError(phone: String): String? {
        val digits = phone.filter { it.isDigit() }
        return when {
            phone.isBlank() -> "Escribe tu número de teléfono."
            !phone.all { it.isDigit() || it in " -()" } -> "Usa solo números."
            digits.length !in 6..14 -> "El número debe tener entre 6 y 14 dígitos."
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

    fun nameError(name: String, what: String): String? = when {
        name.isBlank() -> "Escribe tu $what."
        !name.all { it.isLetter() || it in " '-." } -> "Usa solo letras."
        name.count { it.isLetter() } < 2 -> "Escribe tu $what completo."
        else -> null
    }

    /** Valida la fecha de nacimiento; devuelve el error o null. */
    fun birthDateError(day: String, month: String, year: String, today: LocalDate): String? {
        val d = day.toIntOrNull()
        val m = month.toIntOrNull()
        val y = year.toIntOrNull()
        if (d == null || m == null || y == null) return "Escribe tu fecha de nacimiento completa."
        if (!Numerology.isValidDate(d, m, y)) return "La fecha no es válida."
        if (LocalDate(y, m, d) > today) return "La fecha no puede estar en el futuro."
        return null
    }

    fun genderError(gender: String?): String? = if (gender.isNullOrBlank()) "Elige una opción." else null
}

/** Guarda la cuenta en el dispositivo. La contraseña nunca se guarda en texto: solo su hash con sal. */
object AccountRepository {
    private const val KEY_EMAIL = "account_email"
    private const val KEY_PHONE = "account_phone"
    private const val KEY_COUNTRY = "account_country"
    private const val KEY_SALT = "account_salt"
    private const val KEY_HASH = "account_password_hash"
    private const val KEY_FIRST_NAME = "account_first_name"
    private const val KEY_LAST_NAME = "account_last_name"
    private const val KEY_BIRTH_DATE = "account_birth_date"
    private const val KEY_GENDER = "account_gender"
    private const val KEY_TERMS_VERSION = "terms_accepted_version"
    private const val KEY_TERMS_DATE = "terms_accepted_date"
    private const val ITERATIONS = 10_000

    /** El registro está completo solo si se guardaron los dos pasos. */
    fun isRegistered(): Boolean =
        KeyValueStore.getString(KEY_HASH) != null && KeyValueStore.getString(KEY_FIRST_NAME) != null

    fun current(): UserAccount? {
        val email = KeyValueStore.getString(KEY_EMAIL) ?: return null
        val phone = KeyValueStore.getString(KEY_PHONE) ?: return null
        return UserAccount(
            email = email,
            phone = phone,
            firstName = KeyValueStore.getString(KEY_FIRST_NAME).orEmpty(),
            lastName = KeyValueStore.getString(KEY_LAST_NAME).orEmpty(),
            birthDate = KeyValueStore.getString(KEY_BIRTH_DATE)?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
            gender = KeyValueStore.getString(KEY_GENDER).orEmpty(),
        )
    }

    fun register(credentials: Credentials, profile: Profile) {
        val salt = Sha256.hex(Random.nextBytes(16))
        KeyValueStore.putString(KEY_EMAIL, credentials.email.trim().lowercase())
        KeyValueStore.putString(KEY_PHONE, credentials.fullPhone)
        KeyValueStore.putString(KEY_COUNTRY, credentials.country.iso)
        KeyValueStore.putString(KEY_SALT, salt)
        KeyValueStore.putString(KEY_HASH, hashPassword(credentials.password, salt))
        KeyValueStore.putString(KEY_LAST_NAME, profile.lastName.trim())
        KeyValueStore.putString(KEY_BIRTH_DATE, profile.birthDate.toString())
        KeyValueStore.putString(KEY_GENDER, profile.gender)
        // Se guarda al final: marca el registro como completo.
        KeyValueStore.putString(KEY_FIRST_NAME, profile.firstName.trim())
    }

    /** true si se aceptó la versión actual de los Términos y la Política de Privacidad. */
    fun hasAcceptedCurrentTerms(): Boolean = KeyValueStore.getString(KEY_TERMS_VERSION) == Legal.VERSION

    fun acceptTerms() {
        KeyValueStore.putString(KEY_TERMS_DATE, today().toString())
        KeyValueStore.putString(KEY_TERMS_VERSION, Legal.VERSION)
    }

    /** Fecha (AAAA-MM-DD) en que se aceptaron los textos, o null. */
    fun termsAcceptedOn(): String? = KeyValueStore.getString(KEY_TERMS_DATE)

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
