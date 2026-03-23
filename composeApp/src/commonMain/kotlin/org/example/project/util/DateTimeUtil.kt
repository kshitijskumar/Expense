package org.example.project.util

import kotlinx.datetime.*
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

// Keep platform-specific time function
expect fun DateTimeUtil.getCurrentTimeMillis(): Long

object DateTimeUtil {

    // Common datetime formatting using kotlinx-datetime
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
        val now = kotlin.time.Instant.fromEpochMilliseconds(this.getCurrentTimeMillis())
        val localDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return "${localDate.year}-${localDate.monthNumber.toString().padStart(2, '0')}"
    }
    
    fun getMonthStartTimestamp(month: String): Long {
        val (year, monthNum) = month.split("-").map { it.toInt() }
        val localDate = LocalDate(year, monthNum, 1)
        val localDateTime = localDate.atStartOfDayIn(TimeZone.currentSystemDefault())
        return localDateTime.toEpochMilliseconds()
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
        val startOfDay = localDate.atStartOfDayIn(TimeZone.currentSystemDefault())
        return startOfDay.toEpochMilliseconds()
    }
    
    fun getEndOfDay(timestamp: Long): Long {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endOfDay = LocalDateTime(localDate, LocalTime(23, 59, 59, 999_000_000))
        return endOfDay.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
