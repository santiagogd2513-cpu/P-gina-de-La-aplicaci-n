package com.lunamistica.app

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ContactTest {
    @Test
    fun urlEncoding() {
        assertEquals("Hola%20mundo", Contact.urlEncode("Hola mundo"))
        assertEquals("Ba%C3%B1os", Contact.urlEncode("Baños"))
        assertEquals("a%2Bb%40c.com", Contact.urlEncode("a+b@c.com"))
        assertEquals("%0A", Contact.urlEncode("\n"))
    }

    @Test
    fun whatsappLinkIncludesCategoryAndAccount() {
        val url = Contact.whatsappUrl("Tarot", UserAccount("ana@correo.com", "+57 300 123 4567"))
        assertTrue(url.startsWith("https://wa.me/573160100727?text="))
        assertTrue("Tarot" in url)
        assertTrue("ana%40correo.com" in url)
        assertTrue("%2B57%20300%20123%204567" in url)
    }
}
