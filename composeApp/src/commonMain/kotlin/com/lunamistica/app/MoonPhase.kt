package com.lunamistica.app

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

fun today(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

data class MoonPhase(val name: String, val emoji: String, val advice: String, val ageDays: Double)

object Moon {
    private const val SYNODIC_MONTH = 29.530588853

    /** Luna nueva de referencia: 6 de enero de 2000 (~18:14 UTC). */
    private val reference = LocalDate(2000, 1, 6)

    /** Edad aproximada de la luna (días desde la última luna nueva) al mediodía de [date]. */
    fun age(date: LocalDate): Double {
        val days = (date.toEpochDays() - reference.toEpochDays()).toDouble() - 0.26
        return ((days % SYNODIC_MONTH) + SYNODIC_MONTH) % SYNODIC_MONTH
    }

    fun phase(date: LocalDate = today()): MoonPhase {
        val a = age(date)
        return when {
            a < 1.85 || a >= 27.68 -> MoonPhase("Luna nueva", "🌑", "Momento ideal para sembrar intenciones y empezar de cero.", a)
            a < 5.54 -> MoonPhase("Luna creciente", "🌒", "Buen momento para rituales de atracción y abrir caminos.", a)
            a < 9.23 -> MoonPhase("Cuarto creciente", "🌓", "La energía crece: impulsa proyectos, amor y prosperidad.", a)
            a < 12.92 -> MoonPhase("Gibosa creciente", "🌔", "Perfecciona tus intenciones y refuerza tus baños de abundancia.", a)
            a < 16.61 -> MoonPhase("Luna llena", "🌕", "Energía máxima: carga tus cristales, agradece y celebra.", a)
            a < 20.30 -> MoonPhase("Gibosa menguante", "🌖", "Tiempo de gratitud y de compartir lo aprendido.", a)
            a < 23.99 -> MoonPhase("Cuarto menguante", "🌗", "Suelta lo que ya no te sirve: ideal para limpiezas.", a)
            else -> MoonPhase("Luna menguante", "🌘", "Descansa, purifica tu hogar y cierra ciclos.", a)
        }
    }
}
