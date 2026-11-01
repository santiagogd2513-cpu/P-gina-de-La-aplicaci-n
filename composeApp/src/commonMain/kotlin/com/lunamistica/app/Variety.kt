package com.lunamistica.app

/**
 * Variantes de texto para que las respuestas de la app no sean siempre iguales.
 * Cada vez que se genera una respuesta se elige una combinación al azar.
 */
object Variety {

    // ---------------- Inicio ----------------

    val greetings = listOf("Hola", "Bienvenida", "Qué alegría verte", "Hola de nuevo", "Buen día", "Te estábamos esperando")

    val taglines = listOf(
        "Tu espacio de energía, intuición y bienestar ✨",
        "Hoy el universo tiene un mensaje para ti ✨",
        "Respira, conecta y deja que tu intuición te guíe 🌙",
        "Cada día es una nueva oportunidad para brillar ⭐",
        "Tu energía merece cuidado y atención 💜",
        "Escucha lo que tu alma quiere decirte 🔮",
        "La luna acompaña cada uno de tus pasos 🌕",
    )

    /** Consejos adicionales por fase lunar (se suman al consejo base). */
    val moonAdvice: Map<String, List<String>> = mapOf(
        "Luna nueva" to listOf(
            "Escribe tres deseos y guárdalos bajo una vela blanca.",
            "Es un buen momento para empezar un proyecto o un hábito nuevo.",
            "Siembra tus intenciones con calma y fe.",
        ),
        "Luna creciente" to listOf(
            "Lo que siembres hoy crecerá con fuerza.",
            "Ideal para un baño de prosperidad o de abrir caminos.",
            "Da el primer paso hacia lo que deseas.",
        ),
        "Cuarto creciente" to listOf(
            "Toma decisiones y actúa: la energía te acompaña.",
            "Refuerza tus metas con una afirmación diaria.",
            "Buen momento para rituales de amor y abundancia.",
        ),
        "Gibosa creciente" to listOf(
            "Ajusta tus planes y ten paciencia: casi llegas.",
            "Revisa tus intenciones y hazlas más claras.",
            "Medita unos minutos antes de dormir.",
        ),
        "Luna llena" to listOf(
            "Pon tus cristales bajo la luz de la luna esta noche.",
            "Agradece todo lo que has logrado este ciclo.",
            "Tu intuición está más fuerte que nunca: escúchala.",
        ),
        "Gibosa menguante" to listOf(
            "Comparte lo que sabes y agradece a quienes te acompañan.",
            "Buen momento para ordenar tu casa y tu mente.",
            "Reflexiona sobre lo aprendido este mes.",
        ),
        "Cuarto menguante" to listOf(
            "Suelta lo que ya no te sirve con una limpieza de sal.",
            "Despídete de los hábitos que te pesan.",
            "Ideal para un sahumerio en tu hogar.",
        ),
        "Luna menguante" to listOf(
            "Descansa y recupera tu energía.",
            "Cierra ciclos con amor y gratitud.",
            "Un baño de descarga te ayudará a liberar tensiones.",
        ),
    )

    // ---------------- Tarot ----------------

    val tarotOpeners = listOf(
        "Las cartas te dicen que",
        "El tarot revela que",
        "Este arcano te susurra que",
        "La energía de esta carta indica que",
        "El universo quiere que sepas que",
        "Tu guía espiritual te recuerda que",
        "En este momento de tu vida,",
        "La sabiduría del tarot te muestra que",
        "Presta atención:",
        "Esta carta llega para decirte que",
    )

    val tarotClosers = listOf(
        "Confía en el proceso.",
        "Escucha a tu intuición antes de decidir.",
        "Respira profundo y avanza con calma.",
        "Todo llega en el momento perfecto.",
        "Tú tienes el poder de cambiar tu historia.",
        "Agradece lo que tienes y abre espacio a lo nuevo.",
        "No tengas miedo de mostrar quién eres.",
        "Lo que siembres hoy, lo cosecharás mañana.",
        "Mantén el corazón abierto.",
        "Tu luz interior es más fuerte de lo que crees.",
        "Rodéate de personas que sumen a tu vida.",
        "Date permiso de descansar y sanar.",
    )

    /** Segunda versión del significado de cada arcano: número → (al derecho, invertida). */
    val tarotAlt: Map<Int, Pair<String, String>> = mapOf(
        0 to ("se abre un camino desconocido y emocionante; atrévete a dar el primer paso con alegría."
            to "conviene pensar dos veces antes de lanzarte: la prisa podría jugarte en contra."),
        1 to ("tienes el talento y los recursos para hacer realidad lo que imaginas; es hora de usarlos."
            to "estás dudando de tus capacidades o dispersando tu energía en demasiadas cosas."),
        2 to ("la respuesta que buscas ya vive dentro de ti; el silencio te ayudará a escucharla."
            to "estás ignorando una corazonada importante; vuelve a conectar contigo."),
        3 to ("es tiempo de florecer, crear y recibir; la abundancia llega cuando te cuidas."
            to "has descuidado tu bienestar; necesitas nutrirte antes de dar a los demás."),
        4 to ("el orden y la disciplina te darán la estabilidad que necesitas."
            to "el exceso de control o la rigidez están frenando tu avance."),
        5 to ("un consejo sabio o una tradición te dará el apoyo que necesitas."
            to "es momento de cuestionar las reglas que ya no van contigo."),
        6 to ("una elección del corazón está cerca; escoge lo que esté en armonía con tus valores."
            to "hay desequilibrio en una relación; habla con sinceridad."),
        7 to ("avanzas hacia tu meta con fuerza; mantén el rumbo y triunfarás."
            to "te sientes sin dirección; define bien a dónde quieres llegar."),
        8 to ("tu fuerza está en la paciencia y en la ternura; confía en tu poder interior."
            to "las dudas o el enojo te están quitando energía; busca tu centro."),
        9 to ("un tiempo a solas te dará las respuestas que buscas."
            to "te estás aislando demasiado; abre tu corazón a quienes te quieren."),
        10 to ("la suerte gira a tu favor; se aproximan cambios positivos."
            to "atraviesas una racha difícil, pero todo ciclo termina."),
        11 to ("lo justo llegará; actúa con honestidad y verás resultados."
            to "algo no está en equilibrio; revisa tus decisiones con objetividad."),
        12 to ("hacer una pausa te permitirá ver la situación desde otro ángulo."
            to "llevas demasiado tiempo esperando; es hora de moverte."),
        13 to ("un ciclo termina para que algo mejor comience; suelta sin miedo."
            to "te aferras a algo que ya terminó; dejarlo ir te liberará."),
        14 to ("la calma y el equilibrio traen sanación a tu vida."
            to "estás yendo a los extremos; busca el punto medio."),
        15 to ("hay algo que te ata; reconocerlo es el primer paso para liberarte."
            to "estás rompiendo cadenas y recuperando tu libertad."),
        16 to ("un cambio inesperado sacudirá lo que no era firme, para abrir paso a la verdad."
            to "estás evitando un cambio necesario; enfrentarlo te hará bien."),
        17 to ("la esperanza renace; el universo te guía hacia tus sueños."
            to "has perdido un poco la fe; vuelve a mirar las estrellas."),
        18 to ("no todo es lo que parece; observa con atención tus sueños y presentimientos."
            to "la confusión se aclara y por fin ves la verdad."),
        19 to ("llega una etapa de alegría, éxito y claridad; es tu momento de brillar."
            to "la alegría está ahí, aunque hoy te cueste verla; busca la luz."),
        20 to ("un despertar interior te invita a renacer y perdonar."
            to "eres demasiado dura o duro contigo; date una nueva oportunidad."),
        21 to ("culminas una etapa con éxito; celebra lo logrado."
            to "queda algo pendiente por cerrar antes de empezar lo nuevo."),
    )

    /** Construye el mensaje de una carta combinando una apertura, uno de sus significados y un consejo. */
    fun tarotMessage(card: TarotCard, reversed: Boolean): String {
        val base = if (reversed) card.reversed else card.upright
        val alt = tarotAlt[card.number]?.let { if (reversed) it.second else it.first }
        val core = if (alt != null && kotlin.random.Random.nextBoolean()) {
            "${tarotOpeners.random()} $alt"
        } else {
            base
        }
        return "$core ${tarotClosers.random()}"
    }

    // ---------------- Péndulo ----------------

    val pendulumYes = listOf(
        "El péndulo oscila de adelante hacia atrás: la energía responde que sí.",
        "Un sí claro y firme. El universo apoya lo que preguntaste.",
        "La respuesta es afirmativa: confía y avanza.",
        "Sí. Las energías están alineadas a tu favor.",
        "El movimiento vertical lo confirma: sí, adelante.",
        "Tu guía responde que sí. Sigue tu corazón.",
    )

    val pendulumNo = listOf(
        "El péndulo oscila de lado a lado: la energía responde que no.",
        "La respuesta es no. Tal vez exista un camino mejor para ti.",
        "No por ahora. Ten paciencia y vuelve a preguntar más adelante.",
        "El movimiento horizontal indica un no. Escucha esa señal.",
        "No. El universo te protege de algo que no te conviene.",
        "La energía dice que no. Busca otra alternativa.",
    )

    val pendulumMaybe = listOf(
        "El péndulo gira en círculos: la respuesta aún no está definida.",
        "Quizás. Depende de tus próximas decisiones.",
        "La energía está en movimiento: pregunta de nuevo en unos días.",
        "No hay una respuesta clara todavía. Reformula tu pregunta.",
        "Quizás. Hay factores que aún no se han revelado.",
        "El destino todavía se está escribiendo. Ten paciencia.",
    )

    /** Afirmaciones y prácticas adicionales por chakra (por nombre). */
    val chakraAffirmations: Map<String, List<String>> = mapOf(
        "Raíz" to listOf("Mi cuerpo es mi hogar y en él me siento segura.", "Tengo todo lo que necesito.", "Confío en la vida y en su abundancia."),
        "Sacro" to listOf("Mi creatividad fluye libremente.", "Honro mis emociones.", "Disfruto el placer de estar viva."),
        "Plexo solar" to listOf("Soy capaz de lograr lo que me propongo.", "Mi voluntad es fuerte y clara.", "Me acepto y me valoro tal como soy."),
        "Corazón" to listOf("Perdono y me libero.", "Mi corazón está abierto al amor.", "Merezco amor y lo comparto con alegría."),
        "Garganta" to listOf("Mi voz es importante.", "Digo mi verdad con amor.", "Me comunico con claridad y calma."),
        "Tercer ojo" to listOf("Veo más allá de lo evidente.", "Mi intuición me guía.", "Confío en lo que siento."),
        "Corona" to listOf("Soy uno con el universo.", "Estoy en paz con mi propósito.", "La sabiduría divina fluye a través de mí."),
    )

    val chakraPractices: Map<String, List<String>> = mapOf(
        "Raíz" to listOf("Come alimentos de raíz como zanahoria o remolacha.", "Haz una caminata en la naturaleza.", "Usa algo de color rojo hoy."),
        "Sacro" to listOf("Toma un baño tibio con naranja.", "Pinta, escribe o cocina algo nuevo.", "Mueve las caderas con música que te guste."),
        "Plexo solar" to listOf("Haz tres respiraciones de fuego.", "Cumple hoy una pequeña meta.", "Usa algo amarillo y sonríe al espejo."),
        "Corazón" to listOf("Abraza a alguien que quieras.", "Haz un acto de bondad sin esperar nada.", "Ponte una mano en el pecho y respira despacio."),
        "Garganta" to listOf("Toma una infusión de manzanilla con miel.", "Escribe una carta que nunca enviarás.", "Canta tu canción favorita en voz alta."),
        "Tercer ojo" to listOf("Escribe tus sueños al despertar.", "Medita mirando la llama de una vela.", "Reduce el tiempo frente a pantallas hoy."),
        "Corona" to listOf("Pasa unos minutos en silencio total.", "Agradece tres cosas antes de dormir.", "Medita con música suave."),
    )

    // ---------------- Numerología ----------------

    /** Segunda versión del significado de cada número. */
    val numerologyAlt: Map<Int, String> = mapOf(
        1 to "Eres pionera o pionero: tu energía empuja a empezar, decidir y liderar. Aprende a pedir ayuda cuando la necesites.",
        2 to "Tu don es la armonía: sabes escuchar, unir y acompañar. Recuerda que tu opinión también importa.",
        3 to "Llevas la alegría contigo: comunicas, creas e inspiras. Tu reto es la constancia.",
        4 to "Eres la base firme de quienes te rodean: trabajadora, leal y responsable. Permítete también el descanso y el juego.",
        5 to "Tu alma pide libertad, viajes y experiencias nuevas. El equilibrio te dará la estabilidad que a veces te falta.",
        6 to "Naciste para cuidar, sanar y crear hogar. No olvides que tú también mereces ser cuidada.",
        7 to "Tu mente busca respuestas profundas y tu espíritu, verdad. Confía en tu sabiduría interior.",
        8 to "Tienes el poder de materializar: dinero, éxito y reconocimiento llegan con tu esfuerzo. Usa ese poder con generosidad.",
        9 to "Tu alma es antigua y compasiva; viniste a dar y a enseñar. Aprende a soltar lo que ya cumplió su ciclo.",
        11 to "Eres un canal de luz e intuición. Tu sensibilidad es un don, no una carga.",
        22 to "Tienes la visión para construir algo grande que trascienda. Paso a paso lo lograrás.",
        33 to "Tu misión es el amor que sana. Enseña con el ejemplo y cuida también tu propia energía.",
    )

    val numerologyAdvice = listOf(
        "Consejo: anota este número y obsérvalo durante la semana.",
        "Consejo: medita unos minutos pensando en lo que este número significa para ti.",
        "Consejo: usa esta energía para tomar una decisión pendiente.",
        "Consejo: comparte tu número con alguien de confianza y comparen sus caminos.",
        "Consejo: escribe una afirmación inspirada en este número.",
        "Consejo: observa si este número aparece en tu día a día.",
        "Consejo: agradece las lecciones que este número trae a tu vida.",
    )

    val personalYearAlt: Map<Int, String> = mapOf(
        1 to "Año para atreverte: todo lo que inicies tendrá fuerza.",
        2 to "Año para cultivar vínculos y aprender a esperar.",
        3 to "Año para expresarte, crear y disfrutar de la gente.",
        4 to "Año para construir con disciplina; tu esfuerzo dará frutos.",
        5 to "Año de movimiento: cambios, aventuras y libertad.",
        6 to "Año para el amor, la familia y el hogar.",
        7 to "Año de mirar hacia dentro, estudiar y crecer espiritualmente.",
        8 to "Año de cosechar éxitos y abundancia material.",
        9 to "Año para cerrar ciclos, perdonar y preparar un nuevo comienzo.",
    )

    /** Uno de los significados del número (base o alternativo) más un consejo, al azar. */
    fun numerologyText(number: Int, base: String): String {
        val core = numerologyAlt[number]?.takeIf { kotlin.random.Random.nextBoolean() } ?: base
        return "$core ${numerologyAdvice.random()}"
    }

    fun personalYearText(number: Int, base: String): String =
        personalYearAlt[number]?.takeIf { kotlin.random.Random.nextBoolean() } ?: base

    // ---------------- Rituales y rezos ----------------

    val ritualDone = listOf(
        "Agradece al universo por lo recibido. Así es, y así será.",
        "Tu energía está renovada. Que la luz te acompañe.",
        "Has hecho tu parte; ahora deja que el universo haga la suya.",
        "Respira profundo y siente la paz que te rodea.",
        "Lo que pediste con fe ya está en camino.",
        "Bendiciones para ti y para tu hogar.",
        "Cierra los ojos un momento y da las gracias.",
    )

    val ritualDoneTitles = listOf("Ritual completado", "¡Lo lograste!", "Ritual terminado", "Energía renovada", "Así sea")

    val prayerIntros = listOf(
        "Enciende tu vela, respira y lee con fe.",
        "Busca un lugar tranquilo y lee con el corazón.",
        "Lee despacio, sintiendo cada palabra.",
        "Antes de empezar, cierra los ojos y respira tres veces.",
        "Que esta oración llegue a donde tiene que llegar.",
    )
}
