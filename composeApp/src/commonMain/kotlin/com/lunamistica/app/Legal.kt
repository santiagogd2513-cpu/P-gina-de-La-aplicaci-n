package com.lunamistica.app

/** Una sección de un documento legal: título y párrafos. */
data class LegalSection(val title: String, val paragraphs: List<String>)

data class LegalDocument(val title: String, val sections: List<LegalSection>)

object Legal {
    /**
     * Versión de los textos. Si se cambia, a quien ya aceptó una versión anterior
     * se le vuelven a mostrar para que los acepte de nuevo.
     */
    const val VERSION = "1.0"
    const val UPDATED = "1 de noviembre de 2026"
    private const val CONTACT = "WhatsApp +57 316 010 0727"

    val terms = LegalDocument(
        "Términos y Condiciones",
        listOf(
            LegalSection(
                "1. Aceptación",
                listOf(
                    "Al crear una cuenta y usar Luna Mística (la \"App\") aceptas estos Términos y Condiciones. " +
                        "Si no estás de acuerdo con ellos, no uses la App.",
                ),
            ),
            LegalSection(
                "2. Quién puede usarla",
                listOf(
                    "Debes tener al menos 18 años, o contar con la autorización de tu madre, padre o tutor legal.",
                    "Te comprometes a dar datos verdaderos en el registro y a mantener tu contraseña en secreto.",
                ),
            ),
            LegalSection(
                "3. Naturaleza del contenido",
                listOf(
                    "El tarot, el péndulo, los baños, los rezos, las limpiezas y la numerología se ofrecen como " +
                        "herramientas de orientación espiritual, reflexión personal y bienestar.",
                    "El contenido de la App no sustituye el consejo de profesionales de la salud, la salud mental, " +
                        "el derecho o las finanzas. Ante cualquier problema de salud, consulta a un profesional.",
                    "Las decisiones que tomes a partir de las lecturas y rituales son de tu exclusiva responsabilidad.",
                ),
            ),
            LegalSection(
                "4. Baños, limpiezas y uso de materiales",
                listOf(
                    "Algunos rituales usan plantas, aceites, sal, velas o fuego. Úsalos con precaución: haz una prueba " +
                        "en una pequeña zona de la piel, no ingieras las preparaciones, nunca dejes velas o sahumerios " +
                        "encendidos sin vigilancia y mantenlos lejos de niños y mascotas.",
                    "Si estás embarazada, en lactancia, tienes alergias o alguna condición médica, consulta antes con tu médico.",
                ),
            ),
            LegalSection(
                "5. Contacto por WhatsApp",
                listOf(
                    "Al elegir una categoría, la App abre WhatsApp con un mensaje ya escrito para $CONTACT, que incluye " +
                        "la categoría elegida, tu nombre, tu correo y tu teléfono. El mensaje solo se envía si tú pulsas " +
                        "\"Enviar\" en WhatsApp.",
                    "El uso de WhatsApp se rige por las condiciones y la política de privacidad de WhatsApp.",
                ),
            ),
            LegalSection(
                "6. Uso permitido",
                listOf(
                    "No puedes copiar, vender o distribuir el contenido de la App, ni intentar modificarla, " +
                        "descompilarla o usarla con fines ilegales.",
                    "Los textos, diseños, ilustraciones y el nombre Luna Mística pertenecen a sus autores.",
                ),
            ),
            LegalSection(
                "7. Disponibilidad y cambios",
                listOf(
                    "La App se ofrece gratis y \"tal cual\". Podemos actualizarla, cambiar su contenido o suspenderla " +
                        "en cualquier momento.",
                    "Podemos modificar estos Términos. Si lo hacemos, te los mostraremos de nuevo en la App para que los aceptes.",
                ),
            ),
            LegalSection(
                "8. Limitación de responsabilidad",
                listOf(
                    "En la medida que lo permita la ley, Luna Mística no será responsable por daños derivados del uso " +
                        "o de la imposibilidad de uso de la App, ni de las decisiones tomadas a partir de su contenido.",
                ),
            ),
            LegalSection(
                "9. Contacto y ley aplicable",
                listOf(
                    "Para dudas sobre estos Términos escríbenos por $CONTACT.",
                    "Estos Términos se rigen por las leyes de la República de Colombia.",
                ),
            ),
        ),
    )

    val privacy = LegalDocument(
        "Política de Privacidad",
        listOf(
            LegalSection(
                "1. Datos que recogemos",
                listOf(
                    "Al registrarte: correo electrónico, número de teléfono con su código de país, nombre, apellido, " +
                        "fecha de nacimiento, género y una contraseña.",
                    "La contraseña nunca se guarda tal cual: se guarda cifrada de forma irreversible (hash con sal).",
                    "La App no usa tu ubicación, cámara, micrófono ni contactos, y no incluye publicidad ni rastreadores.",
                ),
            ),
            LegalSection(
                "2. Dónde se guardan",
                listOf(
                    "Tus datos se guardan únicamente en tu dispositivo. La App no los envía a ningún servidor.",
                    "Si borras la App o sus datos, tu cuenta se elimina de ese dispositivo.",
                ),
            ),
            LegalSection(
                "3. Para qué los usamos",
                listOf(
                    "Para crear tu cuenta, personalizar la App (por ejemplo, calcular tu numerología con tu nombre y " +
                        "tu fecha de nacimiento) y preparar el mensaje de contacto por WhatsApp.",
                ),
            ),
            LegalSection(
                "4. Cuándo se comparten",
                listOf(
                    "Solo cuando tú decides enviar el mensaje de WhatsApp que la App prepara al elegir una categoría. " +
                        "En ese caso, la persona que recibe el mensaje en $CONTACT verá tu nombre, correo, teléfono y la " +
                        "categoría elegida, y podrá usarlos para responderte y ofrecerte sus servicios.",
                    "No vendemos ni alquilamos tus datos a terceros.",
                ),
            ),
            LegalSection(
                "5. Tus derechos",
                listOf(
                    "Conforme a la Ley 1581 de 2012 de Colombia, puedes conocer, actualizar, rectificar y solicitar la " +
                        "eliminación de tus datos, y revocar esta autorización.",
                    "Los datos guardados en tu dispositivo puedes borrarlos tú mismo eliminando la App o sus datos. " +
                        "Para los datos que hayas enviado por WhatsApp, escríbenos a $CONTACT.",
                ),
            ),
            LegalSection(
                "6. Menores de edad",
                listOf(
                    "La App está dirigida a mayores de 18 años. Los menores deben usarla con autorización de su madre, " +
                        "padre o tutor legal.",
                ),
            ),
            LegalSection(
                "7. Cambios en esta política",
                listOf(
                    "Si cambiamos esta Política te la mostraremos de nuevo en la App antes de seguir usándola.",
                ),
            ),
        ),
    )
}
