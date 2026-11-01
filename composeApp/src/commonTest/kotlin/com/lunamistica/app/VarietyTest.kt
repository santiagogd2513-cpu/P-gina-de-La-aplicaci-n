package com.lunamistica.app

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VarietyTest {
    @Test
    fun everyCardHasAlternativeMeaning() {
        assertEquals(TarotDeck.majorArcana.map { it.number }.toSet(), Variety.tarotAlt.keys)
    }

    @Test
    fun tarotMessagesVary() {
        val card = TarotDeck.majorArcana.first()
        val messages = (1..60).map { Variety.tarotMessage(card, reversed = false) }.toSet()
        assertTrue(messages.size > 5, "Se esperaban mensajes distintos, hubo ${messages.size}")
        assertTrue(messages.all { it.isNotBlank() })
    }

    @Test
    fun numerologyHasVariantsForEveryNumber() {
        assertEquals(Numerology.meanings.keys, Variety.numerologyAlt.keys)
        assertEquals(Numerology.personalYearMeanings.keys, Variety.personalYearAlt.keys)
        val texts = (1..40).map { Variety.numerologyText(7, Numerology.meanings.getValue(7).text) }.toSet()
        assertTrue(texts.size > 3)
    }

    @Test
    fun moonPhasesHaveExtraAdvice() {
        // Recorre un ciclo lunar completo y comprueba que cada fase tenga consejos extra.
        var date = LocalDate(2025, 3, 29)
        repeat(30) {
            val phase = Moon.phase(date)
            assertTrue(Variety.moonAdvice[phase.name].orEmpty().isNotEmpty(), "Sin consejos para ${phase.name}")
            date = LocalDate.fromEpochDays(date.toEpochDays() + 1)
        }
    }

    @Test
    fun drawnCardsGetAMessage() {
        val drawn = DrawnCard(TarotDeck.majorArcana[5], reversed = true, position = "Presente")
        assertTrue(drawn.message.isNotBlank())
    }
}
