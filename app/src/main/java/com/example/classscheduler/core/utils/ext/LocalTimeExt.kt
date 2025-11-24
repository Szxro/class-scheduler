package com.example.classscheduler.core.utils.ext

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Converts this [LocalTime] to epoch milliseconds using the current date
 * and the system's default time zone.
 *
 * Note: The result depends on:
 * - The current system date.
 * - The current system time zone.
 *
 * @return Epoch time in milliseconds representing today's date at this time.
 */
fun LocalTime.toEpochMilli(): Long {
    val dateTime = this.atDate(LocalDate.now())

    return dateTime.atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
}