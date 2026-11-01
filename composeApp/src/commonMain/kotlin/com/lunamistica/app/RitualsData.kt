package com.lunamistica.app

/** Un baño o una limpieza: materiales y pasos a seguir. */
data class Ritual(
    val id: String,
    val emoji: String,
    val title: String,
    val purpose: String,
    val timing: String,
    val materials: List<String>,
    val steps: List<String>,
    val tips: List<String> = emptyList(),
)

data class Prayer(
    val id: String,
    val emoji: String,
    val title: String,
    val intention: String,
    val text: String,
)

object Rituals {
    val baths = listOf(
        Ritual(
            "prosperidad", "💰", "Baño de prosperidad",
            "Atrae abundancia, buenas oportunidades y estabilidad económica.",
            "Jueves o domingo, en luna creciente o luna llena.",
            listOf("2 litros de agua", "3 ramas de canela", "1 puñado de hojas de laurel", "1 cucharada de miel", "Cáscara de 1 naranja", "1 vela verde o dorada"),
            listOf(
                "Pon a hervir el agua con la canela, el laurel y la cáscara de naranja durante 10 minutos.",
                "Retira del fuego, añade la miel y deja reposar hasta que esté tibia.",
                "Enciende la vela verde y cuela la preparación.",
                "Date tu ducha habitual y al final vierte el preparado desde el cuello hacia abajo.",
                "Mientras lo haces, visualiza tus metas cumplidas y repite: “La abundancia fluye hacia mí”.",
                "Sécate con suaves toques, sin frotar, y deja que la vela se consuma con seguridad.",
            ),
            listOf("Viste ropa clara después del baño.", "Puedes repetirlo durante 3 jueves seguidos."),
        ),
        Ritual(
            "amor", "💗", "Baño de amor propio",
            "Abre el corazón, eleva tu autoestima y atrae relaciones armoniosas.",
            "Viernes, preferiblemente en luna creciente.",
            listOf("2 litros de agua", "Pétalos de 3 rosas rojas o rosadas", "2 ramas de canela", "1 cucharada de miel", "Unas gotas de esencia de vainilla", "1 vela rosa"),
            listOf(
                "Calienta el agua sin que llegue a hervir y añade la canela.",
                "Apaga el fuego, agrega los pétalos, la miel y la vainilla. Deja reposar 15 minutos.",
                "Enciende la vela rosa y agradece el amor que ya existe en tu vida.",
                "Después de tu ducha, vierte el agua desde el pecho hacia abajo.",
                "Coloca las manos sobre tu corazón y repite: “Me amo, me acepto y merezco amor”.",
                "Deja secar al aire unos minutos y recoge los pétalos para devolverlos a la tierra.",
            ),
            listOf("Ideal antes de una cita o un momento importante para ti."),
        ),
        Ritual(
            "proteccion", "🛡️", "Baño de protección",
            "Crea un escudo energético contra envidias y malas vibras.",
            "Martes o sábado, en luna menguante.",
            listOf("2 litros de agua", "1 rama de ruda", "1 rama de romero", "3 cucharadas de sal gruesa", "1 vela blanca"),
            listOf(
                "Hierve el agua con la ruda y el romero durante 10 minutos.",
                "Deja entibiar, cuela y disuelve la sal gruesa.",
                "Enciende la vela blanca y pide protección para ti y los tuyos.",
                "Tras tu ducha, vierte el agua desde la cabeza (evitando los ojos) hacia los pies.",
                "Visualiza una luz blanca que te envuelve por completo.",
                "Desecha el agua sobrante en la tierra o por el desagüe agradeciendo.",
            ),
            listOf("No uses la ruda si estás embarazada.", "Prueba antes en una pequeña zona de la piel."),
        ),
        Ritual(
            "descarga", "🌊", "Baño de descarga",
            "Elimina cargas pesadas, cansancio energético y energía estancada.",
            "Cualquier día en luna menguante, idealmente al anochecer.",
            listOf("2 litros de agua", "1 puñado de albahaca", "1 puñado de hierbabuena", "3 cucharadas de sal marina", "1 limón partido en cuatro"),
            listOf(
                "Machaca con las manos la albahaca y la hierbabuena dentro del agua fría.",
                "Añade la sal marina y el limón. Deja reposar 30 minutos.",
                "Cuela y lleva la preparación a la ducha.",
                "Vierte el agua desde los hombros hacia abajo mientras respiras profundo.",
                "Imagina que todo lo pesado se va por el desagüe.",
                "Termina con una ducha breve de agua limpia.",
            ),
        ),
        Ritual(
            "armonia", "🕊️", "Baño de paz y armonía",
            "Calma la ansiedad, mejora el descanso y equilibra las emociones.",
            "Lunes o cualquier noche que necesites calma.",
            listOf("2 litros de agua", "2 cucharadas de flores de lavanda", "2 cucharadas de manzanilla", "1 vela azul o lila"),
            listOf(
                "Prepara una infusión con la lavanda y la manzanilla durante 10 minutos.",
                "Deja entibiar y cuela.",
                "Apaga las luces y enciende la vela azul.",
                "Vierte el agua lentamente sobre tu cuerpo respirando en cuatro tiempos.",
                "Repite: “Estoy en paz, todo está en orden”.",
                "Ve directo a descansar sin pantallas.",
            ),
        ),
        Ritual(
            "caminos", "🗝️", "Baño para abrir caminos",
            "Desbloquea oportunidades de trabajo, estudios y nuevos proyectos.",
            "Domingo o lunes, en luna nueva o creciente.",
            listOf("2 litros de agua", "1 puñado de hojas de albahaca", "Cáscara de 2 naranjas", "1 cucharada de azúcar morena", "7 clavos de olor", "1 vela amarilla"),
            listOf(
                "Hierve el agua con la cáscara de naranja y los clavos de olor.",
                "Apaga el fuego, añade la albahaca y el azúcar. Deja reposar.",
                "Enciende la vela amarilla y escribe en un papel lo que deseas abrir.",
                "Después de la ducha, vierte el preparado desde el cuello hacia abajo.",
                "Repite: “Todos los caminos se abren para mí”.",
                "Guarda el papel bajo la vela hasta que se consuma.",
            ),
        ),
        Ritual(
            "suerte", "🍀", "Baño de buena suerte",
            "Atrae la fortuna y la energía positiva para un nuevo comienzo.",
            "El primer día del mes o en luna nueva.",
            listOf("2 litros de agua", "1 puñado de hierbabuena", "1 manzana verde en rodajas", "1 cucharada de canela en polvo", "1 vela verde"),
            listOf(
                "Hierve el agua con la manzana y la canela durante 5 minutos.",
                "Añade la hierbabuena, tapa y deja reposar hasta que esté tibia.",
                "Enciende la vela verde y piensa en todo lo bueno que llegará.",
                "Vierte el agua desde el cuello hacia abajo al terminar tu ducha.",
                "Agradece por adelantado la buena suerte que viene a ti.",
            ),
        ),
    )

    val cleansings = listOf(
        Ritual(
            "sahumerio", "🌫️", "Sahumerio del hogar",
            "Purifica la energía de tu casa con el humo de plantas sagradas.",
            "Luna menguante, por la mañana, con las ventanas abiertas.",
            listOf("Palo santo, salvia blanca o romero seco", "Un recipiente resistente al calor", "Cerillos o encendedor", "Una pluma o abanico (opcional)"),
            listOf(
                "Abre puertas y ventanas para que la energía pueda salir.",
                "Enciende el palo santo o la salvia y deja que la llama se apague para que quede humo.",
                "Empieza por la puerta principal y recorre la casa en el sentido de las agujas del reloj.",
                "Pasa el humo por rincones, detrás de las puertas y debajo de los muebles.",
                "Mientras caminas repite: “Todo lo que no es luz se va de este hogar”.",
                "Termina en la puerta principal, apaga el sahumerio y agradece.",
            ),
            listOf("Nunca dejes el sahumerio encendido sin vigilancia.", "Ventila bien si hay niños o mascotas."),
        ),
        Ritual(
            "sal", "🧂", "Limpieza con sal",
            "Absorbe la energía negativa acumulada en los espacios.",
            "Una vez al mes, en luna menguante.",
            listOf("Sal gruesa", "4 vasos o platitos pequeños", "1 vela blanca"),
            listOf(
                "Limpia y ordena la habitación que quieres purificar.",
                "Coloca un vaso con sal gruesa en cada esquina.",
                "Enciende la vela blanca en el centro y declara tu intención.",
                "Deja la sal durante 7 días.",
                "Al terminar, tira la sal por el desagüe con agua corriente, sin tocarla con las manos.",
            ),
            listOf("Si la sal se humedece o se pone oscura, cámbiala antes."),
        ),
        Ritual(
            "huevo", "🥚", "Limpia con huevo",
            "Ritual tradicional para retirar malas energías del cuerpo.",
            "Martes o viernes, preferiblemente por la noche.",
            listOf("1 huevo fresco a temperatura ambiente", "1 vaso de cristal con agua", "1 vela blanca", "Sal"),
            listOf(
                "Enciende la vela blanca y respira profundo tres veces.",
                "Toma el huevo con la mano y pásalo suavemente desde la coronilla hacia abajo.",
                "Recorre la frente, el cuello, los brazos, el pecho, la espalda y las piernas.",
                "Mientras lo pasas, pide que toda carga negativa quede en el huevo.",
                "Rompe el huevo dentro del vaso con agua y observa sus formas.",
                "Añade una pizca de sal y desecha el contenido por el inodoro agradeciendo.",
            ),
            listOf("Se dice que las burbujas y los hilos hacia arriba indican energía que salió."),
        ),
        Ritual(
            "vela", "🕯️", "Limpieza con vela blanca",
            "Ilumina y despeja tu energía personal y tus pensamientos.",
            "Cualquier noche que te sientas cargado.",
            listOf("1 vela blanca", "Aceite de oliva", "Un plato", "Un papel y un lápiz"),
            listOf(
                "Unge la vela con unas gotas de aceite de oliva desde el centro hacia los extremos.",
                "Escribe en el papel lo que deseas soltar.",
                "Coloca el papel bajo el plato y la vela encima.",
                "Enciéndela y contempla la llama en silencio unos minutos.",
                "Repite: “Libero todo lo que ya no me pertenece”.",
                "Deja que la vela se consuma con seguridad y luego desecha el papel.",
            ),
        ),
        Ritual(
            "cristales", "💎", "Limpieza de cristales",
            "Purifica y recarga tus piedras y cuarzos.",
            "Luna llena para recargar; luna menguante para limpiar.",
            listOf("Tus cristales", "Un recipiente con agua y sal", "Un lugar donde les llegue la luz de la luna"),
            listOf(
                "Sumerge los cristales en agua con sal durante unas horas.",
                "Enjuágalos con agua corriente y sécalos con un paño suave.",
                "Colócalos en una ventana durante la noche de luna llena.",
                "Por la mañana tómalos en tus manos y programa tu intención en cada uno.",
            ),
            listOf("Selenita, malaquita y pirita no deben mojarse: límpialas con humo o con luz de luna."),
        ),
        Ritual(
            "respiracion", "🌬️", "Limpieza energética con respiración",
            "Meditación guiada para limpiar tu aura en pocos minutos.",
            "En cualquier momento del día.",
            listOf("Un lugar tranquilo", "5 minutos para ti"),
            listOf(
                "Siéntate cómodo, con la espalda recta y los pies apoyados en el suelo.",
                "Inhala profundo por la nariz contando hasta cuatro.",
                "Exhala lentamente por la boca contando hasta seis. Repite tres veces.",
                "Imagina una luz blanca que entra por tu coronilla y llena tu cuerpo.",
                "Con cada exhalación, visualiza un humo gris que sale de ti y se disuelve.",
                "Siente cómo tu aura brilla limpia y luminosa a tu alrededor.",
                "Agradece, mueve suavemente tus manos y abre los ojos.",
            ),
        ),
    )
}

object Prayers {
    val all = listOf(
        Prayer(
            "proteccion", "🛡️", "Oración de protección",
            "Para pedir protección para ti, tu hogar y tu familia.",
            """
Luz divina que todo lo cubre,
te pido que me envuelvas con tu manto protector.
Que ninguna mala intención, envidia o energía negativa
pueda tocarme a mí, a mi hogar ni a quienes amo.

Que mis caminos estén guardados,
mis pasos sean seguros
y mi corazón permanezca en paz.

Todo lo que no es luz se aleja de mí.
Así es, y así será.
            """.trimIndent(),
        ),
        Prayer(
            "san-miguel", "⚔️", "Oración a San Miguel Arcángel",
            "Oración tradicional de protección espiritual.",
            """
San Miguel Arcángel,
defiéndenos en la batalla.
Sé nuestro amparo contra la perversidad
y las asechanzas del demonio.

Que Dios manifieste sobre él su poder,
es nuestra humilde súplica.
Y tú, Príncipe de la Milicia Celestial,
con el poder que Dios te ha conferido,
arroja al infierno a Satanás
y a los demás espíritus malignos
que vagan por el mundo
para la perdición de las almas.

Amén.
            """.trimIndent(),
        ),
        Prayer(
            "angel", "👼", "Ángel de la guarda",
            "Oración tradicional para pedir compañía y cuidado.",
            """
Ángel de mi guarda,
dulce compañía,
no me desampares
ni de noche ni de día.

No me dejes solo,
que me perdería.
Amén.
            """.trimIndent(),
        ),
        Prayer(
            "abundancia", "💰", "Oración para la abundancia",
            "Para abrir las puertas a la prosperidad y el trabajo.",
            """
Universo generoso, fuente de toda abundancia,
gracias por todo lo que ya tengo.

Abro mi corazón y mis manos para recibir.
Que el trabajo, el dinero y las oportunidades
lleguen a mí de forma justa y bendecida,
para mi bien y el de todos.

Soy merecedor de prosperidad.
Lo que doy regresa multiplicado.
Gracias, gracias, gracias.
            """.trimIndent(),
        ),
        Prayer(
            "amor", "💗", "Oración para el amor",
            "Para atraer y cuidar el amor verdadero.",
            """
Fuerza del amor que habita en todo,
llena mi corazón de ternura y luz.

Sana mis heridas del pasado,
enséñame a amarme y a respetarme,
y trae a mi vida relaciones sinceras,
donde haya alegría, respeto y paz.

Que el amor que doy sea limpio
y el amor que recibo, verdadero.
Así sea.
            """.trimIndent(),
        ),
        Prayer(
            "caminos", "🗝️", "Oración para abrir caminos",
            "Para desbloquear situaciones estancadas.",
            """
Pido que se abran mis caminos
como se abre el día con el sol.

Que se rompa todo bloqueo,
que se aparte todo obstáculo
y que cada puerta cerrada
se convierta en una nueva oportunidad.

Camino con fe, confío en el proceso
y recibo lo que es para mí.
Así es.
            """.trimIndent(),
        ),
        Prayer(
            "salud", "🌿", "Oración por la salud",
            "Para pedir fortaleza y bienestar para ti o un ser querido.",
            """
Energía sanadora del universo,
te pido que fluyas por cada célula de mi cuerpo
(o del cuerpo de quien hoy te encomiendo).

Dale fuerza, alivio y descanso,
calma su dolor y renueva su vitalidad.
Guía las manos de quienes lo cuidan.

Confío en que la salud regresa
y que cada día es un paso hacia el bienestar.
Amén.
            """.trimIndent(),
        ),
        Prayer(
            "gratitud", "🙏", "Oración de gratitud",
            "Para agradecer al terminar el día.",
            """
Gracias por este día,
por lo que aprendí y por lo que viví.
Gracias por las personas que me acompañan,
por el techo que me cubre
y por el pan de mi mesa.

Suelto lo que hoy me pesó
y me quedo con lo bueno.
Que mi descanso sea profundo
y que mañana despierte con luz.
Amén.
            """.trimIndent(),
        ),
    )
}
