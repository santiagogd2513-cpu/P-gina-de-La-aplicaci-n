package com.lunamistica.app

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

/** Configuración de los planes. */
object Plans {
    /** Precio mensual de Premium en pesos colombianos. */
    const val PREMIUM_PRICE_COP = 25_000

    /** Días que dura cada pago de Premium. */
    const val PREMIUM_DAYS = 30

    /** Plan gratuito: número de lecturas de tarot permitidas en cada ventana de tiempo. */
    const val FREE_READINGS = 2
    const val FREE_WINDOW_HOURS = 6
    const val FREE_WINDOW_MS = FREE_WINDOW_HOURS * 60L * 60L * 1000L

    /**
     * Enlace de pago de la pasarela (Wompi, Mercado Pago, PayU, ePayco…).
     * Mientras sea null, la pantalla de pago funciona en MODO DE PRUEBA:
     * no se cobra nada y "Simular pago" activa Premium en el dispositivo.
     */
    val PAYMENT_URL: String? = null

    val isTestMode: Boolean get() = PAYMENT_URL == null

    val premiumBenefits = listOf(
        "Lecturas de tarot ilimitadas, en todas las tiradas",
        "Péndulo, chakras y numerología sin límites",
        "Todos los baños, rezos y limpiezas con modo guiado",
        "Apoyas el crecimiento de Luna Mística 💜",
    )

    val freeBenefits = listOf(
        "$FREE_READINGS lecturas de tarot cada $FREE_WINDOW_HOURS horas",
        "Péndulo, chakras, numerología, baños, rezos y limpiezas",
    )

    /** "$25.000" */
    fun formatCop(amount: Int): String {
        val digits = amount.toString()
        val grouped = digits.reversed().chunked(3).joinToString(".").reversed()
        return "\$" + grouped
    }

    val priceLabel: String get() = formatCop(PREMIUM_PRICE_COP)
}

data class PaymentMethod(val id: String, val emoji: String, val name: String, val detail: String)

val PaymentMethods = listOf(
    PaymentMethod("credito", "💳", "Tarjeta de crédito", "Visa, Mastercard, American Express, Diners"),
    PaymentMethod("debito", "💳", "Tarjeta débito", "Visa Débito, Mastercard Débito"),
    PaymentMethod("pse", "🏦", "PSE", "Débito desde tu cuenta bancaria"),
    PaymentMethod("nequi", "📱", "Nequi", "Paga desde tu app Nequi"),
    PaymentMethod("daviplata", "📱", "Daviplata", "Paga desde tu app Daviplata"),
    PaymentMethod("bancolombia", "🏦", "Botón Bancolombia", "Transferencia desde tu cuenta Bancolombia"),
    PaymentMethod("efectivo", "💵", "Efectivo", "Efecty, Baloto o corresponsales bancarios"),
)

fun nowMillis(): Long = Clock.System.now().toEpochMilliseconds()

/** Plan del usuario y control de uso del plan gratuito, guardados en el dispositivo. */
object PlanRepository {
    private const val KEY_PLAN = "plan"
    private const val KEY_PREMIUM_UNTIL = "plan_premium_until"
    private const val KEY_READINGS = "plan_free_readings"

    fun isPremium(today: LocalDate = today()): Boolean {
        if (KeyValueStore.getString(KEY_PLAN) != "premium") return false
        val until = premiumUntil() ?: return false
        return today <= until
    }

    fun premiumUntil(): LocalDate? =
        KeyValueStore.getString(KEY_PREMIUM_UNTIL)?.let { runCatching { LocalDate.parse(it) }.getOrNull() }

    /** Activa Premium por [Plans.PREMIUM_DAYS] días (en modo de prueba no hay cobro). */
    fun activatePremium(from: LocalDate = today()) {
        KeyValueStore.putString(KEY_PREMIUM_UNTIL, from.plus(DatePeriod(days = Plans.PREMIUM_DAYS)).toString())
        KeyValueStore.putString(KEY_PLAN, "premium")
    }

    fun chooseFree() {
        KeyValueStore.putString(KEY_PLAN, "free")
    }

    private fun readings(): List<Long> =
        KeyValueStore.getString(KEY_READINGS).orEmpty().split(",").mapNotNull { it.toLongOrNull() }

    /** Lecturas que quedan en la ventana actual (null = ilimitadas por ser Premium). */
    fun remainingReadings(now: Long = nowMillis()): Int? =
        if (isPremium()) null else FreeLimit.remaining(readings(), now)

    /** Momento en que se libera la próxima lectura, o null si ya hay disponibles. */
    fun nextReadingAt(now: Long = nowMillis()): Long? = FreeLimit.nextAvailableAt(readings(), now)

    /** Registra una lectura. Devuelve false si el plan gratuito ya no tiene lecturas disponibles. */
    fun tryConsumeReading(now: Long = nowMillis()): Boolean {
        if (isPremium()) return true
        val recent = FreeLimit.inWindow(readings(), now)
        if (recent.size >= Plans.FREE_READINGS) return false
        KeyValueStore.putString(KEY_READINGS, (recent + now).joinToString(","))
        return true
    }
}

/** Lógica pura del límite del plan gratuito (ventana móvil de 6 horas). */
object FreeLimit {
    fun inWindow(timestamps: List<Long>, now: Long): List<Long> =
        timestamps.filter { now - it < Plans.FREE_WINDOW_MS }.sorted()

    fun remaining(timestamps: List<Long>, now: Long): Int =
        (Plans.FREE_READINGS - inWindow(timestamps, now).size).coerceAtLeast(0)

    fun nextAvailableAt(timestamps: List<Long>, now: Long): Long? {
        val recent = inWindow(timestamps, now)
        if (recent.size < Plans.FREE_READINGS) return null
        // Se libera cuando la lectura más antigua de la ventana cumple 6 horas.
        return recent[recent.size - Plans.FREE_READINGS] + Plans.FREE_WINDOW_MS
    }

    /** "3 h 20 min" */
    fun formatWait(ms: Long): String {
        val totalMin = ((ms + 59_999) / 60_000).coerceAtLeast(1)
        val h = totalMin / 60
        val m = totalMin % 60
        return when {
            h == 0L -> "$m min"
            m == 0L -> "$h h"
            else -> "$h h $m min"
        }
    }
}
