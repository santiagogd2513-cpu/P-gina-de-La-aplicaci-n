package com.lunamistica.app

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CountriesTest {
    @Test
    fun defaultIsColombia() {
        assertEquals("CO", Countries.default.iso)
        assertEquals("57", Countries.default.dialCode)
        assertEquals("🇨🇴 +57", Countries.default.label)
    }

    @Test
    fun knownCodes() {
        assertEquals("1", Countries.byIso("US")?.dialCode)
        assertEquals("52", Countries.byIso("MX")?.dialCode)
        assertEquals("34", Countries.byIso("ES")?.dialCode)
        assertEquals("58", Countries.byIso("VE")?.dialCode)
    }

    @Test
    fun noDuplicatesAndSorted() {
        assertEquals(Countries.all.size, Countries.all.map { it.iso }.toSet().size)
        assertTrue(Countries.all.size > 190)
        assertEquals("Afganistán", Countries.all.first().name)
        assertEquals("Zimbabue", Countries.all.last().name)
    }

    @Test
    fun searchIgnoresAccentsAndCode() {
        assertTrue(Countries.search("mexico").any { it.iso == "MX" })
        assertTrue(Countries.search("ESTADOS").any { it.iso == "US" })
        assertTrue(Countries.search("+57").any { it.iso == "CO" })
        assertTrue(Countries.search("peru").any { it.iso == "PE" })
        assertEquals(Countries.all, Countries.search("  "))
    }
}
