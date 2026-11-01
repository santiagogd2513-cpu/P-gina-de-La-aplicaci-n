package com.lunamistica.app

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NumerologyTest {
    @Test
    fun reduceKeepsMasterNumbers() {
        assertEquals(11, Numerology.reduce(29))
        assertEquals(22, Numerology.reduce(22))
        assertEquals(4, Numerology.reduce(22, keepMasters = false))
        assertEquals(1, Numerology.reduce(1999 + 0)) // 1+9+9+9 = 28 -> 10 -> 1
    }

    @Test
    fun pythagoreanLetters() {
        assertEquals(1, Numerology.letterValue('a'))
        assertEquals(1, Numerology.letterValue('J'))
        assertEquals(1, Numerology.letterValue('S'))
        assertEquals(8, Numerology.letterValue('Z'))
        assertEquals(9, Numerology.letterValue('R'))
        assertEquals(5, Numerology.letterValue('Ñ'))
        assertEquals(1, Numerology.letterValue('Á'))
        assertEquals(null, Numerology.letterValue(' '))
    }

    @Test
    fun lifePath() {
        // 15/06/1990: 6 + 6 + (1+9+9+0=19 -> 10 -> 1) = 13 -> 4
        assertEquals(4, Numerology.lifePath(15, 6, 1990))
        // 29/11/1985: 11 + 11 + (23 -> 5) = 27 -> 9
        assertEquals(9, Numerology.lifePath(29, 11, 1985))
    }

    @Test
    fun nameNumbers() {
        // ANA: A1 N5 A1 = 7; vocales 2; consonantes 5
        assertEquals(7, Numerology.expression("Ana"))
        assertEquals(2, Numerology.soul("Ana"))
        assertEquals(5, Numerology.personality("Ana"))
        assertEquals(Numerology.expression("ANA"), Numerology.expression("ána"))
    }

    @Test
    fun personalYearIsOneToNine() {
        for (d in 1..28) for (m in 1..12) {
            assertTrue(Numerology.personalYear(d, m, 2026) in 1..9)
        }
    }

    @Test
    fun everyResultHasMeaning() {
        for (d in 1..28) for (m in 1..12) for (y in 1950..2010) {
            assertTrue(Numerology.lifePath(d, m, y) in Numerology.meanings)
        }
    }

    @Test
    fun dateValidation() {
        assertTrue(Numerology.isValidDate(29, 2, 2000))
        assertFalse(Numerology.isValidDate(29, 2, 2001))
        assertFalse(Numerology.isValidDate(31, 4, 1990))
        assertFalse(Numerology.isValidDate(1, 13, 1990))
    }

    @Test
    fun moonPhases() {
        assertEquals("Luna nueva", Moon.phase(LocalDate(2000, 1, 6)).name)
        // Luna llena conocida: 13 de abril de 2025
        assertEquals("Luna llena", Moon.phase(LocalDate(2025, 4, 13)).name)
        // Luna nueva conocida: 29 de marzo de 2025
        assertEquals("Luna nueva", Moon.phase(LocalDate(2025, 3, 29)).name)
    }
}
