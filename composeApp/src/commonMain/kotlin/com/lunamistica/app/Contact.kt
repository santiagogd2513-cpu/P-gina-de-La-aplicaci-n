package com.lunamistica.app

/** Aviso por WhatsApp al número de contacto cuando alguien elige una categoría. */
object Contact {
    /** Número de WhatsApp que recibe los mensajes (código de país + número, sin "+"). */
    const val WHATSAPP_NUMBER = "573160100727"

    fun message(category: String, account: UserAccount?): String = buildString {
        append("Hola 🌙, me interesa: ").append(category).append(".")
        if (account != null) {
            if (account.fullName.isNotBlank()) append("\nNombre: ").append(account.fullName)
            append("\nCorreo: ").append(account.email)
            append("\nTeléfono: ").append(account.phone)
        }
        append("\n— Enviado desde Luna Mística")
    }

    fun whatsappUrl(category: String, account: UserAccount?): String =
        "https://wa.me/$WHATSAPP_NUMBER?text=" + urlEncode(message(category, account))

    /** Codifica un texto para una URL (UTF-8, estilo RFC 3986). */
    fun urlEncode(text: String): String = buildString {
        for (b in text.encodeToByteArray()) {
            val c = b.toInt() and 0xff
            val ch = c.toChar()
            if (ch in 'A'..'Z' || ch in 'a'..'z' || ch in '0'..'9' || ch in "-_.~") {
                append(ch)
            } else {
                append('%').append("0123456789ABCDEF"[c shr 4]).append("0123456789ABCDEF"[c and 0xf])
            }
        }
    }
}
