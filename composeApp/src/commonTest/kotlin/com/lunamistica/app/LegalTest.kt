package com.lunamistica.app

import kotlin.test.Test
import kotlin.test.assertTrue

class LegalTest {
    @Test
    fun documentsHaveContent() {
        for (doc in listOf(Legal.terms, Legal.privacy)) {
            assertTrue(doc.title.isNotBlank())
            assertTrue(doc.sections.size >= 5)
            assertTrue(doc.sections.all { it.title.isNotBlank() && it.paragraphs.isNotEmpty() })
        }
        assertTrue(Legal.VERSION.isNotBlank())
    }

    @Test
    fun privacyExplainsDataAndWhatsapp() {
        val text = Legal.privacy.sections.flatMap { it.paragraphs }.joinToString(" ")
        assertTrue("correo" in text && "teléfono" in text && "fecha de nacimiento" in text)
        assertTrue("WhatsApp" in text)
        assertTrue("1581" in text)
    }
}
