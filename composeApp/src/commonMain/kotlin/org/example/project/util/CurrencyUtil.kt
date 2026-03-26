package org.example.project.util

object CurrencyUtil {

    private const val INR_CONVERSION = 100.0
    const val INR_CURRENCY_SYMBOL = "₹"

    /**
     * Converts minor currency units (e.g. paise) to a display string with rupees and paise.
     * 560000 → "₹ 5,600"
     * 42250 → "₹ 422.50"
     * 42200 → "₹ 422"
     */
    fun toDisplayAmount(minor: Long): String {
        val isNegative = minor < 0
        val absMajor = kotlin.math.abs(minor) / INR_CONVERSION.toInt()
        val absPaise = kotlin.math.abs(minor) % INR_CONVERSION.toInt()

        // Format rupees with thousands separator
        val digits = absMajor.toString()
        val formattedRupees = buildString {
            digits.reversed().forEachIndexed { i, c ->
                if (i != 0 && i % 3 == 0) append(',')
                append(c)
            }
        }.reversed()

        val sign = if (isNegative) "-" else ""
        val paiseText = if (absPaise != 0L) ".${absPaise.toString().padStart(2, '0')}" else ""
        return "$INR_CURRENCY_SYMBOL $sign$formattedRupees$paiseText"
    }

    /**
     * Converts a user-entered string (e.g. "56.00") to minor units (paise).
     * "56" → 5600
     * "" → 0
     */
    fun toMinorUnits(amount: String): Long {
        if (amount.isBlank()) return 0L
        return (amount.toDoubleOrNull() ?: 0.0).times(INR_CONVERSION).toLong()
    }

    /**
     * Converts minor currency units to a Double in major units.
     * Useful for calculations. 560000 → 5600.0
     */
    fun toCurrency(minor: Long): Double {
        return minor / INR_CONVERSION
    }

    /**
     * Converts minor units to a form input string (for editing).
     * 12550 paise → "125.50"
     * 12500 paise → "125"
     * Strips unnecessary trailing zeros and decimal point.
     */
    fun toFormAmount(minor: Long): String {
        val rupees = minor / INR_CONVERSION.toInt()
        val paise = minor % INR_CONVERSION.toInt()

        return when {
            paise == 0L -> rupees.toString()
            paise < 10 -> "$rupees.0$paise"
            else -> "$rupees.$paise"
        }
    }
}
