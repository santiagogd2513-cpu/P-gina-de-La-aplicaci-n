package com.lunamistica.app

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlansTest {
    private val hour = 60L * 60L * 1000L

    @Test
    fun priceFormat() {
        assertEquals("$25.000", Plans.formatCop(25_000))
        assertEquals("$0", Plans.formatCop(0))
        assertEquals("$1.250.000", Plans.formatCop(1_250_000))
    }

    @Test
    fun freeLimitTwoReadingsPerSixHours() {
        val now = 100 * hour
        assertEquals(2, FreeLimit.remaining(emptyList(), now))
        assertEquals(1, FreeLimit.remaining(listOf(now - 1 * hour), now))
        assertEquals(0, FreeLimit.remaining(listOf(now - 5 * hour, now - 1 * hour), now))
        // Una lectura de hace más de 6 horas ya no cuenta
        assertEquals(1, FreeLimit.remaining(listOf(now - 7 * hour, now - 1 * hour), now))
    }

    @Test
    fun nextAvailableIsWhenOldestReadingExpires() {
        val now = 100 * hour
        assertNull(FreeLimit.nextAvailableAt(listOf(now - 1 * hour), now))
        val next = FreeLimit.nextAvailableAt(listOf(now - 5 * hour, now - 1 * hour), now)
        assertEquals(now + 1 * hour, next)
    }

    @Test
    fun waitFormat() {
        assertEquals("1 h", FreeLimit.formatWait(hour))
        assertEquals("3 h 20 min", FreeLimit.formatWait(3 * hour + 20 * 60_000))
        assertEquals("15 min", FreeLimit.formatWait(15 * 60_000))
        assertEquals("1 min", FreeLimit.formatWait(5_000))
    }
}
