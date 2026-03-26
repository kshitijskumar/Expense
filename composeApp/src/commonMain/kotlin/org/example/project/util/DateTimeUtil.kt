package org.example.project.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

/** Platform clock; kept as extension on [DateTimeUtil] so call sites import `getCurrentTimeMillis`. */
expect fun DateTimeUtil.getCurrentTimeMillis(): Long

object DateTimeUtil {

    fun formatDateHeader(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

        val format = LocalDate.Format {
            dayOfMonth()
            char(' ')
            monthName(MonthNames.ENGLISH_FULL)
        }

        return format.format(localDate)
    }

    fun getCurrentMonth(): String {
        val now = Instant.fromEpochMilliseconds(getCurrentTimeMillis())
        val localDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return "${localDate.year}-${localDate.monthNumber.toString().padStart(2, '0')}"
    }

    fun getYearMonthFromTimestamp(timestamp: Long): Pair<Int, Int> {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return Pair(localDate.year, localDate.month.number)
    }

    fun getMonthStartTimestamp(month: String): Long {
        val (year, monthNum) = month.split("-").map { it.toInt() }
        val localDate = LocalDate(year, monthNum, 1)
        val instant = localDate.atStartOfDayIn(TimeZone.currentSystemDefault())
        return instant.toEpochMilliseconds()
    }

    fun getMonthEndTimestamp(month: String): Long {
        val (year, monthNum) = month.split("-").map { it.toInt() }
        val lastDay = LocalDate(year, monthNum, 1)
            .plus(1, DateTimeUnit.MONTH)
            .minus(1, DateTimeUnit.DAY)
        val localDateTime = LocalDateTime(lastDay, LocalTime(23, 59, 59, 999_000_000))
        return localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun getStartOfDay(timestamp: Long): Long {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return localDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun getEndOfDay(timestamp: Long): Long {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endOfDay = LocalDateTime(localDate, LocalTime(23, 59, 59, 999_000_000))
        return endOfDay.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
