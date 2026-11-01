package com.lunamistica.app

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AccountTest {
    @Test
    fun sha256KnownVectors() {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", Sha256.hex(Sha256.digest(ByteArray(0))))
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", Sha256.hex(Sha256.digest("abc".encodeToByteArray())))
        assertEquals(
            "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
            Sha256.hex(Sha256.digest("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".encodeToByteArray())),
        )
        // Mensaje de 1000 bytes (varios bloques)
        assertEquals(
            "41edece42d63e8d9bf515a9ba6932e1c20cbc9f5a5d134645adb5db1b9737ea3",
            Sha256.hex(Sha256.digest(ByteArray(1000) { 'a'.code.toByte() })),
        )
    }

    @Test
    fun passwordHashUsesSalt() {
        val a = AccountRepository.hashPassword("secreto123", "sal1")
        assertEquals(a, AccountRepository.hashPassword("secreto123", "sal1"))
        assertNotEquals(a, AccountRepository.hashPassword("secreto123", "sal2"))
        assertNotEquals(a, AccountRepository.hashPassword("secreto124", "sal1"))
    }

    @Test
    fun emailRules() {
        assertNull(RegistrationRules.emailError("maria@gmail.com"))
        assertNull(RegistrationRules.emailError(" ana.perez+luna@correo.com.mx "))
        assertNotNull(RegistrationRules.emailError(""))
        assertNotNull(RegistrationRules.emailError("maria@"))
        assertNotNull(RegistrationRules.emailError("maria.gmail.com"))
    }

    @Test
    fun phoneRules() {
        assertNull(RegistrationRules.phoneError("3001234567"))
        assertNull(RegistrationRules.phoneError("300 123 4567"))
        assertNull(RegistrationRules.phoneError("(55) 1234-5678"))
        assertNotNull(RegistrationRules.phoneError(""))
        assertNotNull(RegistrationRules.phoneError("12345"))
        assertNotNull(RegistrationRules.phoneError("300abc4567"))
        assertNotNull(RegistrationRules.phoneError("+57 3001234567"))
    }

    @Test
    fun fullPhoneCombinesCountryCode() {
        val c = Credentials("a@b.co", Countries.default, "316 010 0727", "abc12345")
        assertEquals("+57 3160100727", c.fullPhone)
    }

    @Test
    fun profileRules() {
        val today = LocalDate(2026, 11, 1)
        assertNull(RegistrationRules.nameError("María José", "nombre"))
        assertNull(RegistrationRules.nameError("O'Neil-Peña", "apellido"))
        assertNotNull(RegistrationRules.nameError("", "nombre"))
        assertNotNull(RegistrationRules.nameError("Ana3", "nombre"))
        assertNull(RegistrationRules.birthDateError("15", "6", "1990", today))
        assertNotNull(RegistrationRules.birthDateError("", "6", "1990", today))
        assertNotNull(RegistrationRules.birthDateError("31", "2", "1990", today))
        assertNotNull(RegistrationRules.birthDateError("2", "11", "2026", today))
        assertNull(RegistrationRules.genderError("Femenino"))
        assertNotNull(RegistrationRules.genderError(null))
    }

    @Test
    fun passwordRules() {
        assertNull(RegistrationRules.passwordError("lunita2024"))
        assertNotNull(RegistrationRules.passwordError("corta1"))
        assertNotNull(RegistrationRules.passwordError("solamenteletras"))
        assertNotNull(RegistrationRules.passwordError("12345678"))
        assertNull(RegistrationRules.confirmError("abc12345", "abc12345"))
        assertNotNull(RegistrationRules.confirmError("abc12345", "abc12346"))
    }

    @Test
    fun loginVerification() {
        val salt = "abc"
        val hash = AccountRepository.hashPassword("lunita2024", salt)
        val stored = "ana@correo.com"
        assertEquals(LoginResult.Success, AccountRepository.verifyLogin(stored, hash, salt, "ana@correo.com", "lunita2024"))
        // El correo no distingue mayúsculas ni espacios
        assertEquals(LoginResult.Success, AccountRepository.verifyLogin(stored, hash, salt, " Ana@Correo.com ", "lunita2024"))
        assertEquals(LoginResult.WrongPassword, AccountRepository.verifyLogin(stored, hash, salt, "ana@correo.com", "Lunita2024"))
        assertEquals(LoginResult.NoAccount, AccountRepository.verifyLogin(stored, hash, salt, "otra@correo.com", "lunita2024"))
        assertEquals(LoginResult.NoAccount, AccountRepository.verifyLogin(null, null, null, "ana@correo.com", "lunita2024"))
    }
}
