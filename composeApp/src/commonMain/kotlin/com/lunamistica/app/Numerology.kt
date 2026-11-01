package com.lunamistica.app

/** Cálculos de numerología pitagórica. */
object Numerology {
    private val masterNumbers = setOf(11, 22, 33)
    private const val VOWELS = "AEIOU"

    fun digitSum(n: Int): Int = n.toString().filter { it.isDigit() }.sumOf { it - '0' }

    /** Reduce un número a un dígito, conservando los números maestros (11, 22, 33) si [keepMasters]. */
    fun reduce(n: Int, keepMasters: Boolean = true): Int {
        var x = n
        while (x > 9 && !(keepMasters && x in masterNumbers)) x = digitSum(x)
        return x
    }

    /** Normaliza una letra: quita tildes (Á→A, Ü→U, Ñ→N). Devuelve null si no es una letra A-Z. */
    fun normalize(c: Char): Char? {
        val u = when (c.uppercaseChar()) {
            'Á', 'À', 'Ä', 'Â', 'Ã' -> 'A'
            'É', 'È', 'Ë', 'Ê' -> 'E'
            'Í', 'Ì', 'Ï', 'Î' -> 'I'
            'Ó', 'Ò', 'Ö', 'Ô', 'Õ' -> 'O'
            'Ú', 'Ù', 'Ü', 'Û' -> 'U'
            'Ñ' -> 'N'
            'Ç' -> 'C'
            else -> c.uppercaseChar()
        }
        return u.takeIf { it in 'A'..'Z' }
    }

    /** Valor pitagórico: A J S = 1, B K T = 2, … I R = 9. */
    fun letterValue(c: Char): Int? = normalize(c)?.let { (it - 'A') % 9 + 1 }

    private fun sumLetters(name: String, filter: (Char) -> Boolean): Int =
        name.mapNotNull { normalize(it) }.filter(filter).sumOf { (it - 'A') % 9 + 1 }

    /** Camino de vida: suma reducida del día, mes y año de nacimiento. */
    fun lifePath(day: Int, month: Int, year: Int): Int =
        reduce(reduce(day) + reduce(month) + reduce(digitSum(year)))

    /** Número de expresión (destino): todas las letras del nombre completo. */
    fun expression(name: String): Int = reduce(sumLetters(name) { true })

    /** Número del alma: solo vocales. */
    fun soul(name: String): Int = reduce(sumLetters(name) { it in VOWELS })

    /** Número de personalidad: solo consonantes. */
    fun personality(name: String): Int = reduce(sumLetters(name) { it !in VOWELS })

    /** Número del día de nacimiento. */
    fun birthday(day: Int): Int = reduce(day)

    /** Año personal (1 a 9) para [currentYear]. */
    fun personalYear(day: Int, month: Int, currentYear: Int): Int =
        reduce(reduce(day) + reduce(month) + reduce(digitSum(currentYear)), keepMasters = false)

    fun isValidDate(day: Int, month: Int, year: Int): Boolean {
        if (year !in 1900..2100 || month !in 1..12 || day < 1) return false
        val leap = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
        val days = when (month) {
            2 -> if (leap) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        return day <= days
    }

    data class Meaning(val title: String, val text: String)

    val meanings: Map<Int, Meaning> = mapOf(
        1 to Meaning("El líder", "Independencia, iniciativa y valentía. Naciste para abrir caminos y empezar cosas nuevas. Tu reto es no caer en el orgullo ni en la impaciencia."),
        2 to Meaning("El diplomático", "Sensibilidad, cooperación y armonía. Tienes el don de unir a las personas y de escuchar. Tu reto es confiar en ti y no depender de la aprobación ajena."),
        3 to Meaning("El creativo", "Expresión, alegría y comunicación. Inspiras a los demás con tu creatividad y tu carisma. Tu reto es enfocar tu energía y terminar lo que empiezas."),
        4 to Meaning("El constructor", "Orden, trabajo y estabilidad. Eres confiable y construyes bases sólidas. Tu reto es ser más flexible y permitirte disfrutar."),
        5 to Meaning("El aventurero", "Libertad, cambio y curiosidad. Amas viajar, aprender y experimentar. Tu reto es encontrar el equilibrio y comprometerte."),
        6 to Meaning("El protector", "Amor, responsabilidad y hogar. Cuidas de los demás y buscas la armonía familiar. Tu reto es cuidar también de ti mismo."),
        7 to Meaning("El sabio", "Introspección, espiritualidad y análisis. Buscas la verdad profunda de las cosas. Tu reto es abrirte a los demás y confiar."),
        8 to Meaning("El ejecutivo", "Poder, abundancia y logros materiales. Tienes visión para los negocios y gran fuerza de voluntad. Tu reto es equilibrar lo material y lo espiritual."),
        9 to Meaning("El humanitario", "Compasión, generosidad y sabiduría. Viniste a servir y a cerrar ciclos. Tu reto es soltar el pasado y no cargar con todo."),
        11 to Meaning("Maestro intuitivo", "Número maestro de la iluminación. Tienes una intuición poderosa y la misión de inspirar a otros. Tu reto es manejar tu sensibilidad y tus nervios."),
        22 to Meaning("Maestro constructor", "Número maestro de la materialización. Puedes convertir grandes sueños en realidad para el bien común. Tu reto es no paralizarte ante la presión."),
        33 to Meaning("Maestro sanador", "Número maestro del amor incondicional. Tu camino es enseñar, sanar y servir con compasión. Tu reto es no sacrificarte en exceso."),
    )

    val personalYearMeanings: Map<Int, String> = mapOf(
        1 to "Año de nuevos comienzos: siembra proyectos y toma la iniciativa.",
        2 to "Año de paciencia y relaciones: coopera, escucha y deja que las cosas maduren.",
        3 to "Año de creatividad y vida social: exprésate y disfruta.",
        4 to "Año de trabajo y bases sólidas: organiza, construye y sé constante.",
        5 to "Año de cambios y libertad: viajes, movimiento y nuevas experiencias.",
        6 to "Año del hogar y el amor: familia, compromisos y responsabilidades.",
        7 to "Año de introspección: estudia, medita y conecta con tu espiritualidad.",
        8 to "Año de cosecha y abundancia: logros profesionales y económicos.",
        9 to "Año de cierre de ciclos: suelta, perdona y prepárate para lo nuevo.",
    )
}
