package ru.itmo.market.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object DateUtils {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    
    fun now(): LocalDateTime = LocalDateTime.now()

    fun formatForDisplay(dateTime: LocalDateTime): String = 
        dateTime.format(displayFormatter)

    fun toTimestamp(dateTime: LocalDateTime): Long = 
        dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    fun fromTimestamp(timestamp: Long): LocalDateTime = 
        LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        )

    fun getDayOfWeek(dateTime: LocalDateTime): String = 
        dateTime.dayOfWeek.toString()

    fun isToday(dateTime: LocalDateTime): Boolean = 
        dateTime.toLocalDate() == LocalDateTime.now().toLocalDate()

    fun isBetween(date: LocalDateTime, start: LocalDateTime, end: LocalDateTime): Boolean =
        date.isAfter(start) && date.isBefore(end)

    fun daysBetween(start: LocalDateTime, end: LocalDateTime): Long =
        java.time.temporal.ChronoUnit.DAYS.between(start, end)

    fun minutesBetween(start: LocalDateTime, end: LocalDateTime): Long =
        java.time.temporal.ChronoUnit.MINUTES.between(start, end)
}
