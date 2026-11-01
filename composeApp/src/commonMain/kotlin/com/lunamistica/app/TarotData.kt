package com.lunamistica.app

data class TarotCard(
    val number: Int,
    val name: String,
    val emoji: String,
    val keywords: String,
    val upright: String,
    val reversed: String,
) {
    val roman: String get() = toRoman(number)
}

fun toRoman(n: Int): String {
    if (n == 0) return "0"
    val values = listOf(10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I")
    var rest = n
    return buildString {
        for ((v, s) in values) while (rest >= v) { append(s); rest -= v }
    }
}

object TarotDeck {
    val majorArcana = listOf(
        TarotCard(0, "El Loco", "🎒", "Inicio · libertad · espontaneidad",
            "Un nuevo comienzo te llama. Confía en el camino aunque no veas el final; la vida te invita a arriesgarte con el corazón ligero.",
            "Imprudencia o miedo a dar el salto. Revisa si estás actuando sin pensar o si te estás frenando por inseguridad."),
        TarotCard(1, "El Mago", "✨", "Voluntad · habilidad · manifestación",
            "Tienes todas las herramientas para lograr lo que deseas. Es momento de actuar con decisión y enfocar tu energía.",
            "Talento desaprovechado o manipulación. Cuida que tus intenciones sean claras y honestas."),
        TarotCard(2, "La Sacerdotisa", "🌙", "Intuición · misterio · sabiduría interior",
            "Escucha tu voz interior: ya conoces la respuesta. Hay secretos que se revelarán con paciencia y silencio.",
            "Desconexión de tu intuición o información oculta. Tómate tiempo para meditar antes de decidir."),
        TarotCard(3, "La Emperatriz", "🌸", "Abundancia · fertilidad · creatividad",
            "Etapa fértil y creativa. La abundancia florece cuando te cuidas y nutres lo que amas.",
            "Bloqueo creativo o descuido personal. Recupera el contacto con tu cuerpo y con la naturaleza."),
        TarotCard(4, "El Emperador", "👑", "Estructura · autoridad · estabilidad",
            "Orden, disciplina y liderazgo. Construye bases firmes y toma el control de tu situación.",
            "Rigidez o abuso de poder. Busca el equilibrio entre la firmeza y la flexibilidad."),
        TarotCard(5, "El Hierofante", "📜", "Tradición · guía · aprendizaje",
            "Un maestro, consejo o enseñanza llega a tu vida. Las tradiciones y la fe te dan respaldo.",
            "Necesidad de romper con normas que ya no resuenan contigo. Encuentra tu propia verdad."),
        TarotCard(6, "Los Enamorados", "💞", "Amor · unión · elección",
            "Una conexión profunda o una decisión importante del corazón. Elige alineado con tus valores.",
            "Desarmonía en una relación o indecisión. Sé honesto contigo sobre lo que realmente quieres."),
        TarotCard(7, "El Carro", "🏇", "Victoria · determinación · avance",
            "Avanzas con fuerza hacia tu meta. La determinación te llevará al triunfo.",
            "Falta de dirección o fuerzas que tiran en sentidos opuestos. Define bien tu rumbo."),
        TarotCard(8, "La Fuerza", "🦁", "Valor · paciencia · dominio interior",
            "Tu fuerza está en la calma y la compasión. Domina los retos con suavidad y confianza.",
            "Dudas sobre ti o emociones desbordadas. Recuerda tu poder interior."),
        TarotCard(9, "El Ermitaño", "🏮", "Introspección · búsqueda · soledad",
            "Momento de retirarte y mirar hacia dentro. La luz que buscas está en tu interior.",
            "Aislamiento excesivo o miedo a la soledad. Equilibra el retiro con el contacto humano."),
        TarotCard(10, "La Rueda de la Fortuna", "🎡", "Ciclos · destino · cambio",
            "La rueda gira a tu favor. Llegan cambios y oportunidades inesperadas: aprovéchalas.",
            "Resistencia al cambio o una racha difícil. Todo ciclo termina; prepárate para el siguiente."),
        TarotCard(11, "La Justicia", "⚖️", "Equilibrio · verdad · causa y efecto",
            "Las cosas se ponen en su lugar. Actúa con honestidad y recibirás lo justo.",
            "Injusticia o falta de responsabilidad. Revisa tus decisiones con objetividad."),
        TarotCard(12, "El Colgado", "🙃", "Pausa · entrega · nueva perspectiva",
            "Detenerte te dará claridad. Mira la situación desde otro ángulo y suelta el control.",
            "Estancamiento o sacrificios inútiles. Es hora de tomar una decisión y moverte."),
        TarotCard(13, "La Muerte", "🦋", "Transformación · final · renacimiento",
            "Un ciclo se cierra para que algo nuevo nazca. Suelta lo viejo y abraza la transformación.",
            "Resistencia a soltar. Aferrarte al pasado está frenando tu renacer."),
        TarotCard(14, "La Templanza", "🏺", "Armonía · moderación · sanación",
            "Equilibrio y paciencia. Mezcla las energías opuestas con calma y llegará la sanación.",
            "Excesos o desequilibrio. Vuelve a tu centro y recupera la medida en todo."),
        TarotCard(15, "El Diablo", "⛓️", "Apegos · tentación · sombra",
            "Reconoce aquello que te ata: hábitos, miedos o relaciones. Verlo es el primer paso para liberarte.",
            "Liberación. Estás rompiendo cadenas y recuperando tu poder."),
        TarotCard(16, "La Torre", "⚡", "Ruptura · revelación · despertar",
            "Un cambio repentino derrumba lo que no era sólido. Aunque sacuda, abre espacio para la verdad.",
            "Evitas un cambio necesario o la crisis pierde fuerza. Aprende de lo que se tambalea."),
        TarotCard(17, "La Estrella", "⭐", "Esperanza · inspiración · fe",
            "Renace la esperanza. El universo te guía; confía y deja que tu luz brille.",
            "Desánimo o pérdida de fe. Vuelve a conectar con tus sueños."),
        TarotCard(18, "La Luna", "🌕", "Ilusión · sueños · subconsciente",
            "No todo es lo que parece. Presta atención a tus sueños y a tus miedos ocultos.",
            "La confusión se disipa y la verdad sale a la luz."),
        TarotCard(19, "El Sol", "☀️", "Éxito · alegría · vitalidad",
            "Alegría, claridad y éxito. Todo se ilumina: es un momento para brillar y celebrar.",
            "Alegría pasajera o falta de entusiasmo. Busca la luz incluso en los días nublados."),
        TarotCard(20, "El Juicio", "📯", "Renovación · llamado · perdón",
            "Un despertar interior te llama a renacer. Perdona, evalúa tu camino y da el siguiente paso.",
            "Autocrítica excesiva o ignorar tu llamado. Sé más amable contigo."),
        TarotCard(21, "El Mundo", "🌍", "Plenitud · logro · culminación",
            "Culminas un ciclo con éxito. Te sientes completo y listo para una nueva etapa.",
            "Algo queda pendiente para cerrar el ciclo. Termina lo que empezaste."),
    )
}

enum class Spread(val label: String, val positions: List<String>) {
    Daily("Carta del día", listOf("Tu energía de hoy")),
    One("Una carta", listOf("Respuesta")),
    Three("Pasado · Presente · Futuro", listOf("Pasado", "Presente", "Futuro")),
    Love("Amor", listOf("Tú", "La otra persona", "La relación")),
}

data class DrawnCard(val card: TarotCard, val reversed: Boolean, val position: String)
